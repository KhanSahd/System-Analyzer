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

public class SystemAnalyzerUIRefactored extends Application
{
    private VBox parentContainer;
    private TextField nameInputField;
    private Text greetingText;
    private VBox greetingLayout;

    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage stage )
    {
        parentContainer = createParentContainer();

        nameInputField = createNameInputField();
        parentContainer.getChildren().add( nameInputField );

        greetingText = createGreetingText();
        greetingLayout = createGreetingLayout();

        VBox greetingButton = createGreetingButton();

        parentContainer.getChildren().add( greetingButton );

        Scene scene = new Scene( parentContainer, 1000, 1000 );
        scene.setFill( Color.HONEYDEW );

        stage.setScene( scene );
        stage.show();
    }

    // ---------- UI CREATION ----------

    private VBox createParentContainer()
    {
        VBox container = new VBox( 10 );
        container.setAlignment( Pos.CENTER );
        return container;
    }

    private TextField createNameInputField()
    {
        TextField field = new TextField();
        field.setMaxWidth( 500 );
        field.setAlignment( Pos.CENTER );
        field.setBorder( new Border( new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        ) ) );
        return field;
    }

    private Text createGreetingText()
    {
        Text text = new Text();
        text.setFill( Color.BLUE );
        return text;
    }

    private VBox createGreetingLayout()
    {
        VBox layout = new VBox( 10 );
        layout.setAlignment( Pos.CENTER );
        layout.setPadding( new Insets( 10 ) );
        layout.setMaxSize( Region.USE_PREF_SIZE, Region.USE_PREF_SIZE );

        Button closeButton = createCloseButton();

        layout.getChildren().addAll( greetingText, closeButton );
        return layout;
    }

    private Button createCloseButton()
    {
        Button button = new Button( "Clear Greeting" );
        button.setBackground( Background.fill( Color.BLACK ) );
        button.setTextFill( Color.WHITE );
        button.setCursor( Cursor.HAND );

        button.setOnAction( e ->
        {
            parentContainer.getChildren().remove( greetingLayout );
            parentContainer.getChildren().add( nameInputField );
            parentContainer.getChildren().add( createGreetingButton() );
        } );

        return button;
    }

    private VBox createGreetingButton()
    {
        Text label = new Text( "Click for greeting." );
        label.setFill( Color.WHITE );

        VBox button = new VBox( label );
        button.setAlignment( Pos.CENTER );
        button.setPadding( new Insets( 10 ) );
        button.setBackground( Background.fill( Color.BLUE ) );
        button.setCursor( Cursor.HAND );
        button.setMaxSize( Region.USE_PREF_SIZE, Region.USE_PREF_SIZE );

        Border hoverBorder = new Border( new BorderStroke(
                Color.BLUE,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths( 10 )
        ) );

        button.setOnMouseEntered( e ->
        {
            button.setBackground( Background.fill( Color.WHITE ) );
            label.setFill( Color.BLUE );
            button.setBorder( hoverBorder );
        } );

        button.setOnMouseExited( e ->
        {
            button.setBackground( Background.fill( Color.BLUE ) );
            label.setFill( Color.WHITE );
            button.setBorder( null );
        } );

        button.setOnMouseClicked( e ->
        {
            greetingText.setText( "Hello " + nameInputField.getText() );
            nameInputField.clear();

            parentContainer.getChildren().remove( nameInputField );
            parentContainer.getChildren().remove( button );
            parentContainer.getChildren().add( greetingLayout );
        } );

        return button;
    }
}
