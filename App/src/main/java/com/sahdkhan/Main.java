package com.sahdkhan;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application
{
    public static void main( String[] args )
    {
        launch( args );
    }

    @Override
    public void start( Stage primaryStage )
    {
        SystemAnalyzerUI systemAnalyzerUI = new SystemAnalyzerUI();
        Scene scene = new Scene( systemAnalyzerUI.getRootContainer(), 1000, 1000 );
        scene.setFill( Color.HONEYDEW );
        primaryStage.setScene( scene );
        primaryStage.setTitle( "System Analyzer" );
        primaryStage.show();
    }
}