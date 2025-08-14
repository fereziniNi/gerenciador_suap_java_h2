package org.example.app;

import jakarta.persistence.EntityManager;
import org.example.dao.AlunoDao;
import org.example.model.Aluno;
import org.example.util.JPAUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    // Trabalho feito em Dupla
    // Alunos: Christian Ricci e Nícolas Ferezini
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Scanner scanner = new Scanner(System.in);

        String menu = "\n**GERENCIAMENTO DO SUAP**\n1 - Cadastrar Aluno\n2 - Excluir Aluno\n3 - Alterar Aluno\n4 " +
                "- Buscar aluno pelo nome\n5 - Listar Alunos (com status de aprovação)\n6 - FIM\n";

        EntityManager em = JPAUtil.getEntityManager();
        AlunoDao alunoDao = new AlunoDao(em);


        int opt = 0;
        while (opt != 6){
            System.out.println(menu);
            opt = scanner.nextInt();

            if(opt == 1){
                System.out.println("\nCADASTRO DE ALUNO\n");
                System.out.print("Digite o nome: ");
                String nome = scanner.next();

                System.out.print("Digite o RA: ");
                String ra = scanner.next();

                System.out.print("Digite o email: ");
                String email = scanner.next();

                System.out.print("Digite a nota 1: ");
                BigDecimal nota1 = scanner.nextBigDecimal();

                System.out.print("Digite a nota 2: ");
                BigDecimal nota2 = scanner.nextBigDecimal();

                System.out.print("Digite a nota 3: ");
                BigDecimal nota3 = scanner.nextBigDecimal();

                Aluno aluno = new Aluno(nome, ra, email, nota1, nota2, nota3);
                alunoDao.cadastrarAluno(aluno);
                System.out.println("\nALUNO CADASTRADO\n");
            }
            else if (opt == 2) {
                System.out.println("\nEXCLUIR ALUNO\n");
                System.out.print("Digite o nome: ");
                String nome = scanner.next();
                alunoDao.deletarAluno(nome);
            }
            else if(opt == 3){
                System.out.println("\nALTERAR ALUNO\n");
                System.out.println("Digite o nome: ");
                String nome = scanner.next();
                Aluno escolhido = alunoDao.buscarAlunoPorNome(nome);
                if(escolhido != null){
                    System.out.println("\nDADOS DO ALUNO");
                    String msg = String.format("NOME: %s\nEMAIL: %s\nRA: %s\nNOTAS: %s - %s - %s\n",
                            escolhido.getNome(), escolhido.getEmail(), escolhido.getRa(), escolhido.getNota1(), escolhido.getNota2(),
                            escolhido.getNota3());
                    System.out.println(msg);

                    System.out.println("\nNOVOS DADOS DO ALUNO\n");
                    System.out.print("Digite o nome: ");
                    String nomeNovo = scanner.next();

                    System.out.print("Digite o RA: ");
                    String raNovo = scanner.next();

                    System.out.print("Digite o email: ");
                    String emailNovo = scanner.next();

                    System.out.print("Digite a nota 1: ");
                    BigDecimal nota1Novo = scanner.nextBigDecimal();

                    System.out.print("Digite a nota 2: ");
                    BigDecimal nota2Novo = scanner.nextBigDecimal();

                    System.out.print("Digite a nota 3: ");
                    BigDecimal nota3Novo = scanner.nextBigDecimal();

                    alunoDao.alterarAluno(escolhido, new Aluno(nomeNovo, raNovo, emailNovo, nota1Novo, nota2Novo, nota3Novo));
                }else{
                    System.out.println("ALUNO NAO ENCONTRADO");
                }
            }
            else if(opt == 4){
                System.out.println("\nCONSULTAR ALUNO\n");
                System.out.println("Digite o nome do aluno: ");
                String nome = scanner.next();
                Aluno escolhido = alunoDao.buscarAlunoPorNome(nome);
                if(escolhido != null){
                    System.out.println("\nDADOS DO ALUNO");
                    String msg = String.format("NOME: %s\nEMAIL: %s\nRA: %s\nNOTAS: %s - %s - %s\n",
                            escolhido.getNome(), escolhido.getEmail(), escolhido.getRa(), escolhido.getNota1(), escolhido.getNota2(),
                            escolhido.getNota3());
                    System.out.println(msg);
                }else{
                    System.out.println("ALUNO NAO ENCONTRADO");
                }

            }
            else if(opt == 5){
                System.out.println("\nEXIBINDO TODOS OS ALUNOS\n");
                Map<Aluno, String> map = alunoDao.buscarTodosAlunosStatus();
                for (Map.Entry<Aluno, String> entry : map.entrySet()) {
                    Aluno aluno = entry.getKey();
                    String situacao = entry.getValue();
                    String msg = String.format(
                            "NOME: %s\nEMAIL: %s\nRA: %s\nNOTAS: %s - %s - %s\nSITUAÇÃO: %s\n",
                            aluno.getNome(),
                            aluno.getEmail(),
                            aluno.getRa(),
                            aluno.getNota1(),
                            aluno.getNota2(),
                            aluno.getNota3(),
                            situacao
                    );
                    System.out.println(msg);
                }
            }
        }
        alunoDao.fechar();
    }
}
