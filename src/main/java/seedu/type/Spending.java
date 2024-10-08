package seedu.type;

import seedu.classes.Constants;
import seedu.classes.Ui;
import seedu.exception.WiagiEmptyDescriptionException;
import seedu.exception.WiagiInvalidInputException;

public class Spending {
    private int spendingAmount;
    private String spendingDescription;

    public Spending(String[] userInputWords, String userInput) throws WiagiEmptyDescriptionException,
            WiagiInvalidInputException {
        try {
            int spendingAmount = Integer.parseInt(userInputWords[2]);
            String spendingDescription = getSpendingDescription(spendingAmount, userInput);
            this.spendingAmount = spendingAmount;
            this.spendingDescription = spendingDescription;
            Ui.printWithTab("Entry successfully added!");
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new WiagiInvalidInputException("Did not enter a valid spending amount!");
        }
    }

    private String getSpendingDescription(int spendingAmount, String userInput) throws WiagiEmptyDescriptionException {
        String[] spendingDescriptionWords = userInput.split(Integer.toString(spendingAmount));
        if (spendingDescriptionWords.length == 1) {
            throw new WiagiEmptyDescriptionException();
        }
        return spendingDescriptionWords[1].trim();
    }

    public String toString() {
        return spendingDescription + Constants.LIST_SEPARATOR + spendingAmount;
    }
}
