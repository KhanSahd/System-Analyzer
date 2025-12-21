package com.sahdkhan;

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
    Background cardBackground = new Background( new BackgroundFill(
            Color.valueOf( "#dee2e6" ),
            new CornerRadii( 10 ),
            Insets.EMPTY
    ) );
    DropShadow cardDropShadow = new DropShadow( 12, Color.rgb( 0, 0, 0, 0.25 ) );

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
     * @return VBox with the alignment and spacing set.
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
//        parentContainer.setStyle(
//                "-fx-background-color: " +
////                        "linear-gradient(from 0% 0% to 100% 100%, " +
////                        "rgba(90,85,158,1) 0%, " +
////                        "rgba(148,84,222,1) 29%, " +
////                        "rgba(85,41,207,1) 100%);"
//                        "white;"
//        );
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
//            rootContainer.setRight( rightContainer );
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
        rightCard.setBackground( cardBackground );
        rightCard.setEffect(
                cardDropShadow
        );
        rightCard.setMaxWidth( 360 );
        rightCard.setAlignment( Pos.CENTER );
        rightCard.setSpacing( 10 );
        rightCard.setPadding( new Insets( 15 ) );
        rightCard.getChildren().add( createHeaderTextItem( "Installed Programs" ) );
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
//        installedProgramsSection.setPrefWidth( 200 );
        installedProgramsSection.setPrefHeight( 300 );
        installedProgramsSection.setMaxHeight( 300 );

        installedProgramsSection.setContent( createContentForProgramsSection( installedPrograms ) );
        rightCard.getChildren().add( installedProgramsSection );
        rightWrapper.getChildren().add( rightCard );

        rootContainer.setRight( rightWrapper );
//        installedProgramsSection.setVisible( true );
//        installedProgramsSection.setManaged( true );
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
//        content.setPadding( new Insets( 5 ) );
        content.setStyle( "-fx-background-color: transparent;" );

        installedPrograms.forEach( program ->
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
            programContainer.getChildren().add(
                    createTextItem( program.name(),
                            "-fx-font-size: 13px; -fx-font-weight: bold; -fx-fill: #212529;"
                    )
            );

            programContainer.getChildren().add(
                    createTextItem(
                            program.version(),
                            "-fx-font-size: 11px; -fx-fill: #495057;" )
            );

            programContainer.getChildren().add(
                    createTextItem( program.appPath(),
                            "-fx-font-size: 10px; -fx-fill: #6c757d;" )
            );

            content.getChildren().add( programContainer );
        } );
        return content;
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
        displayInfoBox.setBackground( cardBackground );
        displayInfoBox.setEffect(
                cardDropShadow
        );
        DisplayAndGPUInfoProvider provider = DisplayAndGPUInfoProviderFactory.create();
        displayInfoBox.setAlignment( Pos.TOP_LEFT );
        displayInfoBox.setSpacing( 10 );
        displayInfoBox.setPadding( new Insets( 10 ) );
        displayInfoBox.getChildren().add( createTextItem( "Display and GPU Information" ) );
        displayInfoBox.getChildren().add( new Separator() );
        displayInfoBox.getChildren().add( createTextItem( "GPU" ) );
        GridPane gpuGrid = new GridPane();
        gpuGrid.setHgap( 10 );
        gpuGrid.setVgap( 5 );
        gpuGrid.add( createTextItem( "Model:" ), 0, 0 );
        gpuGrid.add( createTextItem( provider.getModel() ), 1, 0 );
        gpuGrid.add( createTextItem( "Cores:" ), 0, 1 );
        gpuGrid.add( createTextItem( String.valueOf( provider.getCores() ) ), 1, 1 );
        displayInfoBox.getChildren().add( gpuGrid );
        displayInfoBox.getChildren().add( new Separator() );

        displayInfoBox.getChildren().add(
                createTextItem( provider.hasMultipleDisplays() ? "Monitors" : "Monitor" )
        );
        // GridPane to hold the info in two columns
        provider.getMonitors().forEach( monitor ->
        {
            GridPane infoGrid = new GridPane();
            infoGrid.setHgap( 10 );
            infoGrid.setVgap( 5 );
            infoGrid.add( createTextItem( "Name:" ), 0, 0 );
            infoGrid.add( createTextItem( monitor.getName() ), 1, 0 );
            infoGrid.add( createTextItem( "Id:" ), 0, 1 );
            infoGrid.add( createTextItem( String.valueOf( monitor.getId() ) ), 1, 1 );
            infoGrid.add( createTextItem( "Resolution:" ), 0, 2 );
            infoGrid.add( createTextItem( monitor.getResolution() ), 1, 2 );
            infoGrid.add( createTextItem( "Refresh Rate:" ), 0, 3 );
            infoGrid.add( createTextItem( monitor.getRefreshRate() ), 1, 3 );
            displayInfoBox.getChildren().add( infoGrid );
        } );

        StackPane wrapper = new StackPane( displayInfoBox );
        StackPane.setMargin( displayInfoBox, new Insets( 10 ) );
        wrapper.setAlignment( Pos.TOP_LEFT );

        rootContainer.setLeft( wrapper );
    }

    private Text createTextItem( final String textToAdd )
    {
        Text text = new Text( textToAdd );
        text.setFill( Color.BLACK );
        return text;
    }

    private Text createTextItem( final String textToAdd, final Color color )
    {
        Text text = new Text( textToAdd );
        text.setFill( color );
        return text;
    }

    private Text createTextItem( final String textToAdd, final String style )
    {
        Text text = new Text( textToAdd );
        text.setStyle( style );
        return text;
    }

    private Text createHeaderTextItem( final String textToAdd )
    {
        Text text = new Text( textToAdd );
        text.setStyle( "-fx-font-size: 16px;" );
        text.setFill( Color.BLACK );
        return text;
    }

    private Text createHeaderTextItem( final String textToAdd, final Color color )
    {
        Text text = new Text( textToAdd );
        text.setStyle( "-fx-font-size: 16px;" );
        text.setFill( color );
        return text;
    }

}
