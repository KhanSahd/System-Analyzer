package com.sahdkhan.Utilities;

import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Utility class for creating styled Text items in JavaFX.
 */
public class UIHelper
{
    private UIHelper()
    {
        // public static constructor to prevent instantiation
    }

    public static Background getCardBackground()
    {
        return new Background( new BackgroundFill(
                Color.valueOf( "#dee2e6" ),
                new CornerRadii( 10 ),
                Insets.EMPTY
        ) );
    }

    public static DropShadow getCardDropShadow()
    {
        return new DropShadow( 12, Color.rgb( 0, 0, 0, 0.25 ) );
    }

    /**
     * Creates a Text item with default black color.
     *
     * @param textToAdd The text content.
     * @return A Text object with the specified content and black color.
     */
    public static Text createTextItem( final String textToAdd )
    {
        Text text = new Text( textToAdd );
        text.setFill( Color.BLACK );
        return text;
    }

    /**
     * Creates a Text item with the specified color.
     *
     * @param textToAdd The text content.
     * @param color     The color to apply to the text.
     * @return A Text object with the specified content and color.
     */
    public static Text createTextItem( final String textToAdd, final Color color )
    {
        Text text = new Text( textToAdd );
        text.setFill( color );
        return text;
    }

    /**
     * Creates a Text item with the specified style.
     *
     * @param textToAdd The text content.
     * @param style     The CSS style to apply to the text.
     * @return A Text object with the specified content and style.
     */
    public static Text createTextItem( final String textToAdd, final String style )
    {
        Text text = new Text( textToAdd );
        text.setStyle( style );
        return text;
    }

    /**
     * Creates a header Text item with default black color.
     *
     * @param textToAdd The text content.
     * @return A Text object styled as a header with the specified content and black color.
     */
    public static Text createHeaderTextItem( final String textToAdd )
    {
        Text text = new Text( textToAdd );
        text.setStyle( "-fx-font-size: 16px;" );
        text.setFill( Color.BLACK );
        return text;
    }

    /**
     * Creates a header Text item with the specified color.
     *
     * @param textToAdd The text content.
     * @param color     The color to apply to the header text.
     * @return A Text object styled as a header with the specified content and color.
     */
    public static Text createHeaderTextItem( final String textToAdd, final Color color )
    {
        Text text = new Text( textToAdd );
        text.setStyle( "-fx-font-size: 16px;" );
        text.setFill( color );
        return text;
    }
}
