package seedu.commands;

import seedu.classes.Ui;
import seedu.exception.WiagiInvalidIndexException;
import seedu.exception.WiagiInvalidInputException;
import seedu.exception.WiagiMissingParamsException;
import seedu.type.Income;
import seedu.type.IncomeList;
import seedu.type.Spending;
import seedu.type.SpendingList;
import seedu.type.EntryType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static seedu.classes.Constants.FIND_COMMAND_FORMAT;
import static seedu.classes.Constants.INCOME;
import static seedu.classes.Constants.INCORRECT_PARAMS_NUMBER;
import static seedu.classes.Constants.INVALID_CATEGORY;
import static seedu.classes.Constants.INVALID_FIELD;
import static seedu.classes.Constants.WHITESPACE;
import static seedu.classes.Constants.SPENDING;

public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    private static final int LIST_TYPE_INDEX = 1;
    private static final int FIELD_INDEX = 2;
    private static final int VALUE_TO_FIND_INDEX = 3;
    private static final int FIND_ARGUMENTS_LENGTH = 4;
    private static final String AMOUNT_FIELD = "amount";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String DATE_FIELD = "date";

    private final String fullCommand;

    public FindCommand(String fullCommand) {
        this.fullCommand = fullCommand;
    }

    /**
     * Execute editing with the given arguments
     *
     * @param incomes   list of incomes in the application
     * @param spendings list of spendings in the application
     */
    @Override
    public void execute(IncomeList incomes, SpendingList spendings) {
        assert incomes != null;
        assert spendings != null;
        try {
            handleCommand(incomes, spendings);
        } catch (WiagiMissingParamsException | WiagiInvalidInputException | WiagiInvalidIndexException e) {
            Ui.printWithTab(e.getMessage());
        }
    }

    private void handleCommand(IncomeList incomes, SpendingList spendings)
            throws WiagiMissingParamsException, WiagiInvalidIndexException {
        String[] arguments = extractArguments();
        String typeOfList = arguments[LIST_TYPE_INDEX];
        if (!(typeOfList.equals(SPENDING) || typeOfList.equals(INCOME))) {
            throw new WiagiInvalidInputException(INVALID_CATEGORY + FIND_COMMAND_FORMAT);
        }
        switch (typeOfList) {
        case INCOME:
            ArrayList<Income> incomeFindResults = findList(arguments, incomes);
            Ui.printFindResults(incomeFindResults, incomes);
            break;
        case SPENDING:
            ArrayList<Spending> spendingFindResults = findList(arguments, spendings);
            Ui.printFindResults(spendingFindResults, spendings);
            break;
        default:
            throw new WiagiInvalidInputException(INVALID_CATEGORY + FIND_COMMAND_FORMAT);
        }
    }

    private String[] extractArguments() throws WiagiMissingParamsException {
        String[] arguments = fullCommand.split(WHITESPACE, FIND_ARGUMENTS_LENGTH);
        if (arguments.length < FIND_ARGUMENTS_LENGTH) {
            throw new WiagiMissingParamsException(INCORRECT_PARAMS_NUMBER + FIND_COMMAND_FORMAT);
        }
        return arguments;
    }

    private <T extends EntryType> ArrayList<T> findList(String[] arguments, ArrayList<T> list) {
        String findValue = arguments[VALUE_TO_FIND_INDEX];
        assert !findValue.isEmpty() : "Find value should not be empty";
        String field = arguments[FIELD_INDEX];
        switch (field) {
        case AMOUNT_FIELD:
            return getMatchingAmount(findValue, list);
        case DESCRIPTION_FIELD:
            return getMatchingDescription(findValue, list);
        case DATE_FIELD:
            return getMatchingDate(findValue, list);
        default:
            throw new WiagiInvalidInputException(INVALID_FIELD + FIND_COMMAND_FORMAT);
        }
    }

    private <T extends EntryType> ArrayList<T> getMatchingAmount(String findValue, ArrayList<T> list) {
        double lower;
        double upper;
        if (findValue.contains("to")) { // range
            String[] range = findValue.split("to");
            lower = CommandUtils.formatAmount(range[0], FIND_COMMAND_FORMAT);
            upper = CommandUtils.formatAmount(range[1], FIND_COMMAND_FORMAT);
        } else { // exact
            lower = upper = CommandUtils.formatAmount(findValue, FIND_COMMAND_FORMAT);
        }
        return list.stream()
                .filter(entry -> entry.getAmount() >= lower && entry.getAmount() <= upper)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private <T extends EntryType> ArrayList<T> getMatchingDescription(String findValue, ArrayList<T> list) {
        return list.stream()
                .filter(entry -> entry.getDescription().contains(findValue))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private <T extends EntryType> ArrayList<T> getMatchingDate(String findValue, ArrayList<T> list) {
        LocalDate lower;
        LocalDate upper;
        if (findValue.contains("to")) {
            String[] range = findValue.split("to");
            lower = CommandUtils.formatDate(range[0], FIND_COMMAND_FORMAT);
            upper = CommandUtils.formatDate(range[1], FIND_COMMAND_FORMAT);
        } else {
            lower = upper = CommandUtils.formatDate(findValue, FIND_COMMAND_FORMAT);
        }
        return list.stream()
                .filter(entry -> entry.getDate().isAfter(lower.minusDays(1))
                        && entry.getDate().isBefore(upper.plusDays(1)))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
