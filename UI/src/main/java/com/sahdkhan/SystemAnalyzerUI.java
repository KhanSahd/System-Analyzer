package com.sahdkhan;

import com.sahdkhan.Utilities.UIHelper;
import com.sahdkhan.collections.InstalledProgram;
import com.sahdkhan.displayAndGPUInfo.DisplayAndGPUInfoProvider;
import com.sahdkhan.displayAndGPUInfo.DisplayAndGPUInfoProviderFactory;
import com.sahdkhan.programsInfo.InstalledProgramsFactory;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;

import javax.swing.text.Utilities;
import java.util.List;
import java.util.Objects;

/**
 * This class creates the UI for the System Analyzer application.
 */
public class SystemAnalyzerUI
{
    @Getter
    private final BorderPane rootContainer;
    private StackPane loadingPane;

    /**
     * Constructor for SystemAnalyzerUI.
     * Initializes the root container and creates the UI components.
     */
    public SystemAnalyzerUI()
    {
        rootContainer = createRootContainer();
        createUI();
    }

    /**
     * Creates the parent container.
     * This container contains all the components in the scene.
     *
     * @return BorderPane with the alignment and spacing set.
     */
    private BorderPane createRootContainer()
    {
        BorderPane parentContainer = new BorderPane();
        Image backgroundImage = new Image(
                Objects.requireNonNull(
                        getClass().getResource( "/images/bg.jpg" )
                ).toExternalForm()
        );

        BackgroundImage bg = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        100, 100, true, true, false, true
                )
        );

        parentContainer.setBackground(
                new Background( bg )
        );
        return parentContainer;
    }

    /**
     * Creates the UI components and adds them to the root container.
     */
    public void createUI()
    {
        loadingPane = createLoadingPane();
        rootContainer.setCenter( loadingPane );

        loadInstalledProgramsAsync();
        loadDisplayAndGPUInfo();
    }

    /**
     * Creates a loading pane with a spinner.
     * Renders while the installed programs are being loaded.
     *
     * @return StackPane containing the loading spinner.
     */
    private StackPane createLoadingPane()
    {
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setMaxSize( 80, 80 );

        StackPane pane = new StackPane( spinner );
        pane.setAlignment( Pos.CENTER );
        pane.setBackground( new Background( new BackgroundFill(
                Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY
        ) ) );

        return pane;
    }

    /**
     * Loads the installed programs asynchronously.
     * Displays a loading spinner while the programs are being loaded.
     * Once loaded, displays the installed programs section.
     */
    private void loadInstalledProgramsAsync()
    {
        Task< List< InstalledProgram > > task = new Task<>()
        {
            @Override
            protected List< InstalledProgram > call() throws Exception
            {
                return InstalledProgramsFactory.create().getInstalledPrograms();
            }
        };

        task.setOnSucceeded( event ->
        {
            List< InstalledProgram > programs = task.getValue();

            createInstalledProgramsSection( programs );
            loadingPane.setVisible( false );
            loadingPane.setManaged( false );
        } );

        task.setOnFailed( event ->
        {
            Throwable error = task.getException();
            showError( error );
        } );

        Thread thread = new Thread( task, "InstalledProgramsLoader" );
        thread.setDaemon( true );
        thread.start();
    }


    /**
     * Creates the installed programs section.
     * Displays the list of installed programs in a scrollable pane.
     *
     * @param installedPrograms List of installed programs to display.
     */
    private void createInstalledProgramsSection( List< InstalledProgram > installedPrograms )
    {
        // This is the layout wrapper.
        VBox rightWrapper = new VBox();
        rightWrapper.setAlignment( Pos.BOTTOM_CENTER ); // or CENTER
        rightWrapper.setPadding( new Insets( 15 ) );

        // This is the card container that holds the installed programs section including the heading.
        VBox rightCard = new VBox();
        rightCard.setBackground( UIHelper.getCardBackground() );
        rightCard.setEffect(
                UIHelper.getCardDropShadow()
        );
        rightCard.setMaxWidth( 360 );
        rightCard.setAlignment( Pos.CENTER );
        rightCard.setSpacing( 10 );
        rightCard.setPadding( new Insets( 15 ) );
        rightCard.getChildren().add( UIHelper.createHeaderTextItem( "Installed Programs" ) );
        rightCard.getChildren().add( new Separator() );
        ScrollPane installedProgramsSection = new ScrollPane();
        installedProgramsSection.setHbarPolicy( ScrollPane.ScrollBarPolicy.AS_NEEDED );
        installedProgramsSection.setVbarPolicy( ScrollPane.ScrollBarPolicy.NEVER );
        installedProgramsSection.setStyle( "-fx-background-color: transparent;" );
        installedProgramsSection.skinProperty().addListener( ( obs, oldSkin, newSkin ) ->
        {
            Node viewport = installedProgramsSection.lookup( ".viewport" );
            if ( viewport != null )
            {
                viewport.setStyle( "-fx-background-color: transparent;" );
            }
        } );
        installedProgramsSection.setPannable( false );
        installedProgramsSection.setFitToWidth( true );
        installedProgramsSection.setPrefHeight( 300 );
        installedProgramsSection.setMaxHeight( 300 );

        installedProgramsSection.setContent( createContentForProgramsSection( installedPrograms ) );
        rightCard.getChildren().add( installedProgramsSection );
        rightWrapper.getChildren().add( rightCard );

        rootContainer.setRight( rightWrapper );
    }

    /**
     * Creates the content for the installed programs section.
     *
     * @param installedPrograms List of installed programs to display.
     * @return VBox containing the installed programs.
     */
    private VBox createContentForProgramsSection( List< InstalledProgram > installedPrograms )
    {
        VBox content = new VBox();
        content.setSpacing( 0 );
        content.setStyle( "-fx-background-color: transparent;" );

        installedPrograms.forEach( program ->
        {
            VBox programContainer = createProgram();
            programContainer.getChildren().add(
                    UIHelper.createTextItem( program.name(),
                            "-fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #212529;"
                    )
            );

            programContainer.getChildren().add(
                    UIHelper.createTextItem(
                            program.version(),
                            "-fx-font-size: 11px; -fx-fill: #495057;" )
            );

            content.getChildren().add( programContainer );
        } );
        return content;
    }

    private static VBox createProgram()
    {
        VBox programContainer = new VBox( 2 );
        programContainer.setAlignment( Pos.CENTER_LEFT );
        programContainer.setPadding( new Insets( 8, 10, 8, 10 ) );
        programContainer.setBorder(
                new Border( new BorderStroke(
                        Color.rgb( 0, 0, 0, 0.08 ),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths( 0, 0, 1, 0 )
                ) )
        );
        return programContainer;
    }

    private void showError( Throwable error )
    {
        Text errorText = new Text( "Failed to load installed programs:\n" + error.getMessage() );
        errorText.setFill( Color.RED );

        StackPane errorPane = new StackPane( errorText );
        errorPane.setAlignment( Pos.CENTER );

        rootContainer.setRight( errorPane );
    }

    private void loadDisplayAndGPUInfo()
    {
        VBox displayInfoBox = new VBox();
        displayInfoBox.setMaxHeight( Region.USE_PREF_SIZE );
        displayInfoBox.setBackground( UIHelper.getCardBackground() );
        displayInfoBox.setEffect(
                UIHelper.getCardDropShadow()
        );
        DisplayAndGPUInfoProvider provider = DisplayAndGPUInfoProviderFactory.create();
        displayInfoBox.setAlignment( Pos.TOP_LEFT );
        displayInfoBox.setSpacing( 10 );
        displayInfoBox.setPadding( new Insets( 10 ) );
        displayInfoBox.getChildren().add( UIHelper.createTextItem( "Display and GPU Information" ) );
        displayInfoBox.getChildren().add( new Separator() );
        displayInfoBox.getChildren().add( UIHelper.createTextItem( "GPU" ) );
        GridPane gpuGrid = new GridPane();
        gpuGrid.setHgap( 10 );
        gpuGrid.setVgap( 5 );
        gpuGrid.add( UIHelper.createTextItem( "Model:" ), 0, 0 );
        gpuGrid.add( UIHelper.createTextItem( provider.getModel() ), 1, 0 );
        if ( provider.getCores() != null )
        {
            gpuGrid.add( UIHelper.createTextItem( "Cores:" ), 0, 1 );
            gpuGrid.add( UIHelper.createTextItem( String.valueOf( provider.getCores() ) ), 1, 1 );
        }
        displayInfoBox.getChildren().add( gpuGrid );
        displayInfoBox.getChildren().add( new Separator() );

        displayInfoBox.getChildren().add(
                UIHelper.createTextItem( provider.hasMultipleDisplays() ? "Monitors" : "Monitor" )
        );
        // GridPane to hold the info in two columns
        provider.getMonitors().forEach( monitor ->
        {
            GridPane infoGrid = new GridPane();
            infoGrid.setHgap( 10 );
            infoGrid.setVgap( 5 );
            infoGrid.add( UIHelper.createTextItem( "Name:" ), 0, 0 );
            infoGrid.add( UIHelper.createTextItem( monitor.getName() ), 1, 0 );
            infoGrid.add( UIHelper.createTextItem( "Id:" ), 0, 1 );
            infoGrid.add( UIHelper.createTextItem( String.valueOf( monitor.getId() ) ), 1, 1 );
            infoGrid.add( UIHelper.createTextItem( "Resolution:" ), 0, 2 );
            infoGrid.add( UIHelper.createTextItem( monitor.getResolution() ), 1, 2 );
            infoGrid.add( UIHelper.createTextItem( "Refresh Rate:" ), 0, 3 );
            infoGrid.add( UIHelper.createTextItem( monitor.getRefreshRate() ), 1, 3 );
            displayInfoBox.getChildren().add( infoGrid );
        } );

        StackPane wrapper = new StackPane( displayInfoBox );
        StackPane.setMargin( displayInfoBox, new Insets( 10 ) );
        wrapper.setAlignment( Pos.TOP_LEFT );

        rootContainer.setLeft( wrapper );
    }

}
