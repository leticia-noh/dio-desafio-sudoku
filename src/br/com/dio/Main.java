package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        final String[] jogoPronto = "0,0;4,false 1,0;7,false 2,0;9,true 3,0;5,false 4,0;8,true 5,0;6,true 6,0;2,true 7,0;3,false 8,0;1,false 0,1;1,false 1,1;3,true 2,1;5,false 3,1;4,false 4,1;7,true 5,1;2,false 6,1;8,false 7,1;9,true 8,1;6,true 0,2;2,false 1,2;6,true 2,2;8,false 3,2;9,false 4,2;1,true 5,2;3,false 6,2;7,false 7,2;4,false 8,2;5,true 0,3;5,true 1,3;1,false 2,3;3,true 3,3;7,false 4,3;6,false 5,3;4,false 6,3;9,false 7,3;8,true 8,3;2,false 0,4;8,false 1,4;9,true 2,4;7,false 3,4;1,true 4,4;2,true 5,4;5,true 6,4;3,false 7,4;6,true 8,4;4,false 0,5;6,false 1,5;4,true 2,5;2,false 3,5;3,false 4,5;9,false 5,5;8,false 6,5;1,true 7,5;5,false 8,5;7,true 0,6;7,true 1,6;5,false 2,6;4,false 3,6;2,false 4,6;3,true 5,6;9,false 6,6;6,false 7,6;1,true 8,6;8,false 0,7;9,true 1,7;8,true 2,7;1,false 3,7;6,false 4,7;4,true 5,7;7,false 6,7;5,false 7,7;2,true 8,7;3,false 0,8;3,false 1,8;2,false 2,8;6,true 3,8;8,true 4,8;5,true 5,8;1,false 6,8;4,true 7,8;7,false 8,8;9,false".split(" ");

        final Map<String, String> positions = Stream.of(jogoPronto)
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        k -> k.split(";")[1])
                );

        int option = -1;
        while (true) {
            System.out.println("Selecione uma das opções a seguir:");
            System.out.println("1 - Iniciar um novo jogo");
            System.out.println("2 - Adicionar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar o jogo atual");
            System.out.println("5 - Verificar o status do jogo");
            System.out.println("6 - Reiniciar o jogo");
            System.out.println("7 - Finalizar o jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");


            }
        }

    }

    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado\n");
            return;
        }

        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                String positionConfig = positions.get("%s,%s".formatted(i, j));
                int expected = Integer.parseInt(positionConfig.split(",")[0]);
                boolean fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);

                Space currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }

        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar!\n");
        showCurrentGame();
        System.out.println();
    }

    private static void inputNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado\n");
            return;
        }

        System.out.print("Informe a coluna em que o número será inserido: ");
        int col = runUntilGetValidNumber(0, 8);

        System.out.print("Agora, informe a linha em que o número será inserido: ");
        int row = runUntilGetValidNumber(0, 8);

        System.out.printf("Por fim, informe o número a ser adicionado na posição [%s,%s]: ", col, row);
        int value = runUntilGetValidNumber(1, 9);

        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s, %s] tem um valor fixo\n\n", col, row);
        }
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado\n");
            return;
        }

        System.out.print("Informe a coluna do número a ser removido: ");
        int col = runUntilGetValidNumber(0, 8);

        System.out.print("Agora, informe a linha do número a ser removido:  ");
        int row = runUntilGetValidNumber(0, 8);

        if (!board.clearValue(col, row)) {
            System.out.printf("A posição [%s,%s] tem um valor fixo\n\n", col, row);
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado\n\n");
            return;
        }

        Object[] args = new Object[81];
        int argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (List<Space> col : board.getSpaces()) {
                args[argPos++] = " " + ((isNull(col.get(i).getCurrent())) ? " " : col.get(i).getCurrent());
            }
        }

        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void showGameStatus() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado\n");
            return;
        }

        System.out.println("O jogo atualmente apresenta o status: " + board.getStatus().getLabel());
        if (board.hasErrors()) {
            System.out.println("O jogo contém erros\n");
        }
        else {
            System.out.println("O jogo não contém erros\n");
        }

    }

    private static void clearGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado\n");
            return;
        }

        System.out.println("Tem certeza de que deseja reiniciar o jogo (s/n)?");
        String confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("s") && !confirm.equalsIgnoreCase("n")) {
            System.out.println("Informe uma resposta válida (s ou n): ");
            confirm = scanner.next();
        }
        if (confirm.equalsIgnoreCase("s")) {
            board.reset();
            System.out.println("O jogo foi reiniciado!\n");
        }
        else if (confirm.equalsIgnoreCase("n")) {
            System.out.println("Retomando o jogo!\n");
        }
    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado\n");
            return;
        }

        if (board.gameIsFinished()) {
            System.out.println("Parabéns! Você completou o jogo!\n");
            showCurrentGame();
            board.reset();
            board = null;
        }
        else if (board.hasErrors()) {
            System.out.println("Seu jogo contém erros :(\nVerifique novamente os números escolhidos!\n");
        }
        else {
            System.out.println("Ainda existe(m) espaço(s) em branco!\n");
        }
    }

    private static int runUntilGetValidNumber(final int min, final int max) {
        int current = scanner.nextInt();
        while (current < min && current > max) {
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }
}
