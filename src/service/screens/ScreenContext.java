package src.service.screens;

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
