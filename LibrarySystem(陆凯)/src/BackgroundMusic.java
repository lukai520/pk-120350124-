import java.awt.EventQueue;

import javax.swing.JFrame;
import java.applet.Applet;
import java.applet.AudioClip;
import java.io.*;
import java.awt.Frame;
import java.net.*; //

import javax.swing.*;
import java.awt.event.*;

public class BackgroundMusic extends JFrame implements WindowListener{

		URL url;
		AudioClip ac;
	public BackgroundMusic() {
		super();
		setBounds(100, 100, 20, 20);
		
			
	
			File f=new File("music/home.wav");
			try {
				url = f.toURL();
				ac=Applet.newAudioClip(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	public void stopMusic()
	{
		ac.stop();
	}
	public void startMusic()
	{
	
			
			ac.loop();
			
		this.setVisible(false);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
