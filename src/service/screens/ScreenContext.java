/**
 * The ScreenContext class manages the current screen in the application and provides
 * methods to interact with it. It acts as a context for the screen, allowing the 
 * application to switch between different screens and handle their behavior.
 */
package src.service.screens;

import src.service.screens.ScreenInterfaces.Screen;

public class ScreenContext {

	private Screen currScreen;

	public ScreenContext(Screen newScreen){
		this.currScreen = newScreen;
	}

	public void displayScreen() {
        if (currScreen != null) {
            currScreen.displayAndProgress();
        } else {
            System.out.println("No screen set.");
        }
    }

	public Screen getScreen(){
		return this.currScreen;
	}

	public void setScreen(Screen newScreen){
		this.currScreen = newScreen;
	}

	public Character getLastInput(){
		return this.currScreen.getLastInput();
	}
	
}
