/**
 * Helper class to create the Question object
 *
 * @author: Koulick Sankar Paul
 *
 * NetID: ksp160330
 *
 * version: 2.0: 11/26/2017
 */

package classes;

public class Question {

    private String question;
    private String optionChoiceOne;
    private String optionChoiceTwo;
    private String optionChoiceThree;
    private String optionChoiceFour;

    private Question() {}

    /**
     * Constructor for Question class
     *
     * @param question
     * @param optionChoiceOne
     * @param optionChoiceTwo
     * @param optionChoiceThree
     * @param optionChoiceFour
     */
    public Question(String question, String optionChoiceOne,
                    String optionChoiceTwo, String optionChoiceThree,
                    String optionChoiceFour) {

        this.question = question;
        this.optionChoiceOne = optionChoiceOne;
        this.optionChoiceTwo = optionChoiceTwo;
        this.optionChoiceThree = optionChoiceThree;
        this.optionChoiceFour = optionChoiceFour;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionChoiceOne() {
        return optionChoiceOne;
    }

    public String getOptionChoiceTwo() {
        return optionChoiceTwo;
    }

    public String getOptionChoiceThree() {
        return optionChoiceThree;
    }

    public String getOptionChoiceFour() {
        return optionChoiceFour;
    }
}