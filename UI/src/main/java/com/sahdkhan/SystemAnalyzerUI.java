package com.sahdkhan;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SystemAnalyzerUI extends Application
{
    private VBox parentContainer;
    private TextField nameInputField;
    private Text greetingText;
    private VBox greetingLayout;
    private VBox greetingButtonContainer;

    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage primaryStage )
    {
        parentContainer = creatParentContainer();

        nameInputField = createNameInputField();

        // Creating components
        greetingText = createGreetingText();

        // Layout for getGreetingButton
        greetingButtonContainer = createGetGreetingButton();

        // Layout for greeting
        greetingLayout = createGreetingLayout();

        // Adding greetingButtonContainer to parentContainer
        parentContainer.getChildren().add( greetingButtonContainer );

        // Adding parentContainer to scene.
        Scene scene = new Scene( parentContainer, 1000, 1000 );
        scene.setFill( Color.HONEYDEW );
        primaryStage.setScene( scene );
        primaryStage.show();
    }

    /**
     * Creates the container that holds the "Get Greeting" Button.
     * <br>
     * Creation of the container involves:
     * <ul>
     *     <li>Setting the background, alignment, maxSize, and padding for the container itself.</li>
     *     <li>Creating a button and setting the text and color of the text within the button</li>
     *     <li>Adding events to the button such as mouseEntered, mouseExited, and mouseClicked</li>
     * </ul>
     * @return VBox that contains the button to get the greeting.
     */
    private VBox createGetGreetingButton()
    {
        VBox greetingButtonContainer = new VBox( 10 );
        greetingButtonContainer.setAlignment( Pos.CENTER );
        greetingButtonContainer.setMaxSize( Region.USE_PREF_SIZE, Region.USE_PREF_SIZE );
        greetingButtonContainer.setPadding( new Insets( 10 ) );

        // Creating the button
        Button getGreetingButton = new Button( "Click for greeting." );
        getGreetingButton.setTextFill( Color.WHITE );
        getGreetingButton.setBackground( new Background( new BackgroundFill(
                        Color.BLUE,
                        CornerRadii.EMPTY,
                        Insets.EMPTY ) ) );

        BorderStroke borderStroke = new BorderStroke(
                Color.BLUE,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths( 1.0 )
        );

        getGreetingButton.setOnMouseEntered( event ->
        {
            getGreetingButton.setBackground( new Background( new BackgroundFill(
                    Color.WHITE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY ) )  );
            getGreetingButton.setTextFill( Color.BLUE );
            getGreetingButton.setBorder( new Border( borderStroke ) );
        } );

        getGreetingButton.setCursor( Cursor.HAND );

        getGreetingButton.setOnMouseExited( event ->
        {
            getGreetingButton.setBackground( new Background( new BackgroundFill(
                    Color.BLUE,
                    CornerRadii.EMPTY,
                    Insets.EMPTY ) )  );
            getGreetingButton.setTextFill( Color.WHITE );
            getGreetingButton.setBorder( null );
        } );

        getGreetingButton.setOnMouseClicked( event ->
        {
            greetingText.setText( "Hello " + nameInputField.getText() );
            nameInputField.clear();

            nameInputField.setVisible( false );
            nameInputField.setManaged( false );
            greetingButtonContainer.setVisible( false );
            greetingButtonContainer.setManaged( false );

            greetingLayout.setVisible( true );
            greetingLayout.setManaged( true );
        } );

        // Add the button the container
        greetingButtonContainer.getChildren().add( getGreetingButton );
        return greetingButtonContainer;
    }

    /**
     * Creates the container that holds the greeting text and the clear button.
     * @return VBox with the greetingText, clearButton, and styles applied.
     */
    private VBox createGreetingLayout()
    {
        VBox greetingLayout = new VBox( 10 );
        greetingLayout.getChildren().add( greetingText );
        greetingLayout.setSpacing( 10 );
        greetingLayout.setAlignment( Pos.CENTER );
        greetingLayout.setMaxSize( Region.USE_PREF_SIZE, Region.USE_PREF_SIZE );
        greetingLayout.setPadding( new Insets( 10 ) );

        Button clearButton = createClearGreetingButton();
        greetingLayout.getChildren().add( clearButton );
        parentContainer.getChildren().add( greetingLayout );
        greetingLayout.setVisible( false );
        greetingLayout.setManaged( false );
        return greetingLayout;
    }

    private Button createClearGreetingButton()
    {
        // Adding button to close the greeting.
        Button clearButton = new Button( "Clear Greeting" );
        clearButton.setBackground( new Background( new BackgroundFill(
                Color.BLACK,
                CornerRadii.EMPTY,
                Insets.EMPTY ) )  );
        clearButton.setTextFill( Color.WHITE );
        clearButton.setCursor( Cursor.HAND );

        clearButton.setOnMouseClicked( event ->
        {
            greetingLayout.setVisible( false );
            greetingLayout.setManaged( false );

            nameInputField.setVisible( true );
            nameInputField.setManaged( true );
            greetingButtonContainer.setVisible( true );
            greetingButtonContainer.setManaged( true );
        } );
        return clearButton;
    }

    private TextField createNameInputField()
    {
        TextField nameInputField = new TextField();
        nameInputField.setMaxWidth( 500 );
        nameInputField.setMaxHeight( 100 );
        nameInputField.setAlignment( Pos.CENTER );
        BorderStroke borderStroke = new BorderStroke( Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT );
        nameInputField.setBorder( new Border( borderStroke ) );
        parentContainer.getChildren().add( nameInputField );
        nameInputField.setVisible( true );
        nameInputField.setManaged( true );
        return nameInputField;
    }

    /**
     * Creates the parent container.
     * This container contains all the components in the scene.
     * @return VBox with the alignment and spacing set.
     */
    private VBox creatParentContainer()
    {
        VBox parentContainer = new VBox();
        parentContainer.setAlignment( Pos.CENTER );
        parentContainer.setSpacing( 10 );
        return parentContainer;
    }

    private Text createGreetingText()
    {
        Text greeting = new Text();
        greeting.setFill( Color.BLUE );
        return greeting;
    }
}
