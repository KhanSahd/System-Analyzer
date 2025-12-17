package com.sahdkhan;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class UI extends Application
{
    private static UI INSTANCE = new UI();
    private JFrame mainWindow;

    private UI()
    {
        initializeUI();
    }

    @Override
    public void start( Stage primaryStage ) throws Exception
    {
        
    }

    public static UI getInstance()
    {
        return INSTANCE;
    }

    private void initializeUI()
    {
        mainWindow = new JFrame( "System Analyzer" );
        mainWindow.setPreferredSize( new Dimension( 500, 500 ) );

    }

}
