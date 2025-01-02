package com.book.jpa;

import com.book.jpa.domain.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class JpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaApplication.class, args);
	}

	@Component
	class MyRunner implements CommandLineRunner {
		@Override
		public void run(String... args) throws Exception {
			runSomething();
		}
	}

	public void runSomething() {

		System.out.println("애플리케이션이 시작되면 이 메소드가 실행됩니다!");

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");

		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			logic(em);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();

	}

	private void logic(EntityManager em) {
		String id = "id1";
		Member member = new Member();
		member.setId(id);
		member.setUsername("지수");
		member.setAge(3);

		em.persist(member);

		member.setAge(20);

		Member findMember = em.find(Member.class, id);
		System.out.println("findMember = " + findMember.getUsername() + ", age = " + findMember.getAge());

		List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

		System.out.println(members.size());

		em.remove(member);
	}

}
