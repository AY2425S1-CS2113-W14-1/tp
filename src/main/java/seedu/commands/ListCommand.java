package seedu.commands;

import seedu.classes.Ui;
import seedu.type.Income;
import seedu.type.IncomeList;
import seedu.type.Spending;
import seedu.type.SpendingList;

import java.util.ArrayList;

public class ListCommand extends Command {
    /**
     * Prints all incomes and spendings contained in the given IncomeList and SpendingList.
     * @param incomes IncomeList containing all incomes in the application.
     * @param spendings SpendingList containing all the spending in the application.
     */
    @Override
    public void execute(IncomeList incomes, SpendingList spendings) {
        Ui.printWithTab("Incomes:");
        for (int i = 0; i < incomes.size(); i++) {
            Ui.printWithTab(i + ". " + incomes.get(i).toString());
        }
        Ui.printWithTab("Spendings:");
        for (int i = 0; i < spendings.size(); i++) {
            Ui.printWithTab(i + ". " + spendings.get(i).toString());
        }
    }
}
