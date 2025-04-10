
/**
 * The InnerInput interface defines the structure for handling inner queries
 * within a screen interface. It provides methods for displaying a query
 * and validating user input.
 */
package src.service.screens.ScreenInterfaces;

public interface InnerInput {
	void displayInnerQuery();

	Boolean checkInnerQuery(Integer input);
}
