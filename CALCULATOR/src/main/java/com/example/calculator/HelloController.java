package com.example.calculator;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class HelloController {
    public TextField tfInput;
    public StringBuilder sb;
    //numbers
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public Button btn4;
    public Button btn5;
    public Button btn6;
    public Button btn7;
    public Button btn8;
    public Button btn9;
    public Button btn0;
    //OPS
    public Button btnMinus;
    public Button btnPlus;
    public Button btnDivide;
    public Button btnMultiply;
    //additional OPS
    public Button btnDelete;
    public Button btnClear;
    public Button btnResult;
    public Button btnDot;
    public Button btnSign;

    //OPS
    @FXML
    protected void onResultButtonClick() {
        Double res = eval(tfInput.getText());
        tfInput.setText(String.valueOf(res));
    }
    @FXML
    protected void onMinusButtonClick() {
        tfInput.appendText("-");
    }
    @FXML
    protected void onMultiplyButtonClick() {
        tfInput.appendText("*");
    }
    @FXML
    protected void onDivideButtonClick() {
        tfInput.appendText("/");
    }
    @FXML
    protected void onPlusButtonClick() {
        tfInput.appendText("+");
    }
    //OPS
    @FXML
    protected void onClearButtonClick() {
        tfInput.clear();
    }
    @FXML
    protected void onDeleteButtonClick() {
        sb.append(tfInput.getText());
        sb.deleteCharAt(sb.length()-1);
        tfInput.setText(sb.toString());
        sb.delete(0, sb.length());
    }
    @FXML
    protected void onDotButtonClick() {
        tfInput.appendText(".");
    }
    @FXML
    protected void onSignButtonClick() {
        if(Integer.parseInt(tfInput.getText()) > 0){
            sb.append("-").append(tfInput.getText());
        } else {
            sb.append(tfInput.getText());
            sb.deleteCharAt(0);
        }
        tfInput.setText(sb.toString());
        sb.delete(0, sb.length());
    }

    //Numbers
    @FXML
    protected void on1ButtonClick() {
        tfInput.appendText("1");
    }
    @FXML
    protected void on2ButtonClick() {
        tfInput.appendText("2");
    }
    @FXML
    protected void on3ButtonClick() {
        tfInput.appendText("3");
    }
    @FXML
    protected void on4ButtonClick() {
        tfInput.appendText("4");
    }
    @FXML
    protected void on5ButtonClick() {
        tfInput.appendText("5");
    }
    @FXML
    protected void on6ButtonClick() {
        tfInput.appendText("6");
    }
    @FXML
    protected void on7ButtonClick() {
        tfInput.appendText("7");
    }
    @FXML
    protected void on8ButtonClick() {
        tfInput.appendText("8");
    }
    @FXML
    protected void on9ButtonClick() {
        tfInput.appendText("9");
    }
    @FXML
    protected void on0ButtonClick() {
        tfInput.appendText("0");
    }

    @FXML
    public void initialize(){
        btnClear.setOnMouseClicked(e -> {
            tfInput.clear();
        });
        sb = new StringBuilder();
        tfInput.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    onResultButtonClick();
                }
            }
        });
    }


    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) throw new RuntimeException("Missing ')'");
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}