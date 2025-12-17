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
    private String name = "";

    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage primaryStage )
    {
        VBox parentContainer = new VBox(); // All components live inside this.
        parentContainer.setAlignment( Pos.CENTER );
        parentContainer.setSpacing( 10 );

        TextField nameInputField = new TextField();
        nameInputField.setMaxWidth( 500 );
        nameInputField.setMaxHeight( 100 );
        nameInputField.setAlignment( Pos.CENTER );

        BorderStroke borderStroke = new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        );
        Border border = new Border( borderStroke );
        nameInputField.setBorder( border );

        parentContainer.getChildren().add( nameInputField );

        // Creating components
        Text greeting = new Text();
        greeting.setFill( Color.BLUE );

        Text appTitle = new Text( "Click for greeting." );
        appTitle.setFill( Color.WHITE );

        // Layout for getGreetingButton
        VBox getGreetingButton = new VBox( 10 );
        getGreetingButton.setBackground( Background.fill( Color.BLUE ) );
        getGreetingButton.getChildren().add( appTitle );
        getGreetingButton.setAlignment( Pos.CENTER );
        getGreetingButton.setMaxSize( Region.USE_PREF_SIZE, Region.USE_PREF_SIZE );
        getGreetingButton.setPadding( new Insets( 10, 10, 10, 10 ) );

        // Layout for greeting
        VBox greetingLayout = new VBox( 10 );
        greetingLayout.getChildren().add( greeting );
        greetingLayout.setSpacing( 10 );
        greetingLayout.setAlignment( Pos.CENTER );
        greetingLayout.setMaxSize( Region.USE_PREF_SIZE, Region.USE_PREF_SIZE );
        greetingLayout.setPadding( new Insets( 10, 10, 10, 10 ) );

        // Adding button to close the greeting.
        Button closeButton = new Button( "Clear Greeting" );
        closeButton.setBackground( Background.fill( Color.BLACK ) );
        closeButton.setTextFill( Color.WHITE );
        closeButton.setCursor( Cursor.HAND );
        greetingLayout.getChildren().add( closeButton );

        closeButton.setOnMouseClicked( event ->
        {
            parentContainer.getChildren().remove( greetingLayout );
            parentContainer.getChildren().add( nameInputField );
            parentContainer.getChildren().add( getGreetingButton );
        } );

        // Border for appTitle
        borderStroke = new BorderStroke(
                Color.BLUE,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths( 10.0, 10.0, 10.0, 10.0 )
        );
        Border appTitleBorder = new Border( borderStroke );

        // appTitle events
        getGreetingButton.setOnMouseEntered( event ->
        {
            getGreetingButton.setBackground( Background.fill( Color.WHITE ) );
            appTitle.setFill( Color.BLUE );
            getGreetingButton.setBorder( appTitleBorder );
        } );

        getGreetingButton.setCursor( Cursor.HAND );

        getGreetingButton.setOnMouseExited( event ->
        {
            getGreetingButton.setBackground( Background.fill( Color.BLUE ) );
            appTitle.setFill( Color.WHITE );
            getGreetingButton.setBorder( null );
        } );

        getGreetingButton.setOnMouseClicked( event ->
        {
            name = nameInputField.getText();
            greeting.setText( "Hello " + name );
            nameInputField.clear();

            parentContainer.getChildren().remove( nameInputField );
            parentContainer.getChildren().remove( getGreetingButton );

            if ( !parentContainer.getChildren().contains( greetingLayout ) )
            {
                parentContainer.getChildren().add( greetingLayout );
            }
        } );

        // Adding appTitleLayout to parentContainer
        parentContainer.getChildren().add( getGreetingButton );

        // Adding parentContainer to scene.
        Scene scene = new Scene( parentContainer, 1000, 1000 );
        scene.setFill( Color.HONEYDEW );
        primaryStage.setScene( scene );
        primaryStage.show();
    }
}
