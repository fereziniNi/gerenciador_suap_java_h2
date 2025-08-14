package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.example.model.Aluno;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AlunoDao {
    private EntityManager em;
    public AlunoDao(EntityManager em) {
        this.em = em;
    }

    public void cadastrarAluno(Aluno aluno){
        this.em.getTransaction().begin();
        this.em.persist(aluno);
        this.em.getTransaction().commit();
    }

    public Aluno buscarAlunoPorNome(String nome){
        try {
            String jpql = "SELECT a FROM Aluno a WHERE a.nome = :nome";
            return em.createQuery(jpql, Aluno.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deletarAluno(String nome){
        Aluno aluno = buscarAlunoPorNome(nome);
        if(aluno != null){
            em.remove(aluno);
            em.getTransaction().commit();
            System.out.println("ALUNO REMOVIDO COM SUCESSO!");
        }else{
            System.out.println("NAO FOI POSSIVEL ENCONTRAR O ALUNO");
        }
    }

    public void alterarAluno(Aluno antes, Aluno depois){
        this.em.getTransaction().begin();
        depois.setId(antes.getId());
        em.merge(depois);
        this.em.getTransaction().commit();
        System.out.println("ALUNO ATUALIZADO COM SUCESSO!");
    }

    public Map<Aluno, String> buscarTodosAlunosStatus(){
        String jpql = "SELECT a FROM Aluno a";
        List<Aluno> alunoList = em.createQuery(jpql, Aluno.class).getResultList();
        Map<Aluno, String> finalList = new HashMap<>();
        for(Aluno a: alunoList){
            BigDecimal media = a.getNota1()
                    .add(a.getNota2())
                    .add(a.getNota3())
                    .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);

            String situacao;
            if((media.compareTo(BigDecimal.valueOf(4)) < 0)){
                situacao = "REPROVADO";
            } else if (media.compareTo(BigDecimal.valueOf(6)) < 0) {
                situacao = "RECUPERAÇÃO";
            }else{
                situacao = "APROVADO";
            }

            finalList.put(a, situacao);
        }
        return finalList;
    }

    public void fechar(){
        this.em.close();
    }
}

