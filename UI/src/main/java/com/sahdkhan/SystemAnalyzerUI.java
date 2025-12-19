package com.sahdkhan;

import com.sahdkhan.collections.InstalledProgram;
import com.sahdkhan.programs.Programs;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class SystemAnalyzerUI
{
    private final BorderPane rootContainer;
    private ScrollPane installedProgramsSection;
    private StackPane loadingPane;

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
        parentContainer.setStyle(
                "-fx-background-color: " +
                        "linear-gradient(from 0% 0% to 100% 100%, " +
                        "rgba(90,85,158,1) 0%, " +
                        "rgba(148,84,222,1) 29%, " +
                        "rgba(85,41,207,1) 100%);"
        );
        return parentContainer;
    }

    public void createUI()
    {
        createInstalledProgramsButton();
    }

    private void createInstalledProgramsButton()
    {
        Button getInstalledProgramsButton = new Button( "Get Installed Programs" );
        getInstalledProgramsButton.setTextFill( Color.WHITE );
        getInstalledProgramsButton.setBackground( new Background( new BackgroundFill(
                Color.BLACK,
                CornerRadii.EMPTY,
                Insets.EMPTY
        ) ) );

        getInstalledProgramsButton.setOnMouseClicked( e ->
        {
            getInstalledProgramsButton.setVisible( false );
            getInstalledProgramsButton.setManaged( false );
            loadingPane = createLoadingPane();
            rootContainer.setRight( loadingPane );

            loadInstalledProgramsAsync();
        } );

        rootContainer.setCenter( getInstalledProgramsButton );
    }

    private StackPane createLoadingPane()
    {
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setMaxSize( 80, 80 );

        StackPane pane = new StackPane( spinner );
        pane.setAlignment( Pos.CENTER );
        pane.setBackground( new Background( new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY
        ) ) );

        return pane;
    }

    private void loadInstalledProgramsAsync()
    {
        Task< List< InstalledProgram > > task = new Task<>()
        {
            @Override
            protected List< InstalledProgram > call() throws IOException
            {
                return Programs.getInstalledPrograms();
            }
        };

        task.setOnSucceeded( event ->
        {
            List< InstalledProgram > programs = task.getValue();
            createInstalledProgramsSection( programs );
            rootContainer.setRight( installedProgramsSection );
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


    private void createInstalledProgramsSection( List< InstalledProgram > installedPrograms )
    {
        installedProgramsSection = new ScrollPane();
        installedProgramsSection.setHbarPolicy( ScrollPane.ScrollBarPolicy.NEVER );
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
        installedProgramsSection.setPannable( true );
        installedProgramsSection.setFitToWidth( true );
//        installedProgramsSection.setPrefWidth( 200 );
        installedProgramsSection.setMinWidth( Region.USE_PREF_SIZE );
        installedProgramsSection.setMaxWidth( Region.USE_PREF_SIZE );

        installedProgramsSection.setContent( createContentForProgramsSection( installedPrograms ) );

        rootContainer.setRight( installedProgramsSection );
        installedProgramsSection.setVisible( true );
        installedProgramsSection.setManaged( true );
    }

    private VBox createContentForProgramsSection( List< InstalledProgram > installedPrograms )
    {
        VBox content = new VBox();
        content.setAlignment( Pos.CENTER );
        content.setSpacing( 5 );
        content.setPadding( new Insets( 5 ) );
        content.setStyle( "-fx-background-color: transparent;" );

        Background programContainerBackground = new Background( new BackgroundFill(
                Color.BLACK,
                new CornerRadii( 10 ),
                Insets.EMPTY
        ) );

        installedPrograms.forEach( program ->
        {
            VBox programContainer = new VBox();
            programContainer.setAlignment( Pos.CENTER );
            programContainer.setBackground( programContainerBackground );

            Text name = new Text( program.getName() );
            name.setFill( Color.WHITE );
            programContainer.getChildren().add( name );

            Text version = new Text( program.getVersion() );
            version.setFill( Color.WHITE );
            programContainer.getChildren().add( version );

            Text path = new Text( program.getAppPath() );
            path.setStyle( "-fx-font-size: 10px; -fx-fill: #bbbbbb;" );
            programContainer.getChildren().add( path );

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

    public BorderPane getRootContainer()
    {
        return rootContainer;
    }
}
