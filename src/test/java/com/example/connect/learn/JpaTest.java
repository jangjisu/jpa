package com.example.connect.learn;

import com.example.connect.api.domain.item.Movie;
import com.example.connect.api.domain.member.Member;
import com.example.connect.api.domain.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
class JpaTest {
    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    @DisplayName("1차 캐시를 통한 객체 동일성 보장")
    void sameTest() {
        EntityManager em = emf.createEntityManager();

        try (em) {
            EntityTransaction et = em.getTransaction();
            et.begin(); // 트랜잭션 시작

            Member member = Member.create("01012345678", "홍길동");

            em.persist(member);

            Member member1 = em.find(Member.class, member.getId());
            Member member2 = em.find(Member.class, member.getId());

            assertThat(member1).isEqualTo(member2);
            assertThat(member1).isSameAs(member2);

            et.commit();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("변경 감지를 통한 엔티티 수정")
    void dirtyCheckTest () {
        EntityManager em = emf.createEntityManager();

        try (em) {
            EntityTransaction et = em.getTransaction();
            et.begin(); // 트랜잭션 시작

            Member member = Member.create("01012345678", "홍길동");

            em.persist(member);

            member.setName("김길동");
            member.setPhoneNum("01098765432");

            em.flush();

            et.commit();

            Member findMember = em.find(Member.class, member.getId());

            assertThat(findMember.getName()).isEqualTo("김길동");
            assertThat(findMember.getPhoneNum()).isEqualTo("01098765432");

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("쓰기 지연 확인 (GenerationType 이 IDENTITY 가 아닐때)")
    void transactionalWriteBehind () {
        EntityManager em = emf.createEntityManager();

        try (em) {
            EntityTransaction et = em.getTransaction();
            em.setFlushMode(FlushModeType.COMMIT);
            et.begin();

            Member member = Member.create("01012345678", "홍길동");
            Member member2 = Member.create("01023456789", "김철수");

            em.persist(member);
            em.persist(member2);

            List<Member> members = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            assertThat(members).isEmpty();

            em.flush();

            List<Member> members2 = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            assertThat(members2).hasSize(2)
                    .extracting("userTelNo", "userName")
                    .containsExactly(
                            tuple("01012345678", "홍길동"),
                            tuple("01023456789", "김철수")
                    );

            et.commit();


        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("영속상태 확인")
    void persistenceTest () {
        EntityManager em = emf.createEntityManager();

        try (em) {
            EntityTransaction et = em.getTransaction();
            et.begin();

            //case 1
            Member member = Member.create("01033333333", "홍길동");

            boolean isPersistence = em.contains(member);

            assertThat(isPersistence).isFalse();

            //case2
            em.persist(member);

            boolean isPersistence2 = em.contains(member);

            assertThat(isPersistence2).isTrue();

            //case3
            em.detach(member);

            boolean isPersistence3 = em.contains(member);

            assertThat(isPersistence3).isFalse();

            //case4
            Member member2 = Member.create("01044444444", "김철수");
            Member mergedMember = em.merge(member2);

            boolean isPersistence4 = em.contains(mergedMember);

            assertThat(isPersistence4).isTrue();

            //case5
            em.clear();

            boolean isPersistence5 = em.contains(member2);

            assertThat(isPersistence5).isFalse();

            et.commit();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @DisplayName("JPQL 쿼리 수행해보기")
    @Test
    void JPQLQuery() {
        EntityManager em = emf.createEntityManager();

        try {
            String jpql = "select m from Member as m where m.name = 'kim'";
            List<Member> resultList = em.createQuery(jpql, Member.class).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @DisplayName("Criteria 쿼리 사용해보기")
    @Test
    void criteriaQuery() {
        EntityManager em = emf.createEntityManager();

        try {
            //criteria 사용준비
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            //루트 클래스 (조회 시작할 클래스)
            Root<Member> m = query.from(Member.class);

            //쿼리 생성
            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("name"), "kim"));
            List<Member> resultList = em.createQuery(cq).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @DisplayName("QueryDSL 사용해보기")
    @Test
    void QueryDSLQuery() {
        EntityManager em = emf.createEntityManager();

        try {
            JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
            QMember member = QMember.member;
            List<Member> members = jpaQueryFactory
                    .selectFrom(member)
                    .where(member.name.eq("kim"))
                    .fetch();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @DisplayName("NativeQuery 사용해보기")
    @Test
    void NativeQuery() {
        EntityManager em = emf.createEntityManager();

        try {
            String sql = "SELECT * FROM MEMBER WHERE NAME = 'kim'";
            List<Member> members = em.createNativeQuery(sql, Member.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @DisplayName("")
    @Test
    void test() {
        EntityManager em = emf.createEntityManager();

        try {
            // given
            Movie movie = new Movie();
            em.persist(movie);

            em.detach(movie);

            em.merge(movie);

            // when

            // then
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @DisplayName("")
    @Test
    void 영속성컨텍스트와_프록시() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            // given
            Member newMember = new Member("0102345678", "회원1");
            em.persist(newMember);
            em.flush();
            em.clear();

            // when
            Member refMember = em.getReference(Member.class, newMember.getId());
            Member findMember = em.find(Member.class, newMember.getId());

            System.out.println("refMember = " + refMember.getClass());
            System.out.println("findMember = " + findMember.getClass());

            //refMember = class com.example.connect.api.domain.member.Member$HibernateProxy$WwDAuiVc
            //findMember = class com.example.connect.api.DFdomain.member.Member$HibernateProxy$WwDAuiVc

            // then
            assertThat(refMember).isSameAs(findMember);
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @DisplayName("")
    @Test
    void 영속성컨텍스트와_프록시역순() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            // given
            Member newMember = new Member("0102345678", "회원1");
            em.persist(newMember);
            em.flush();
            em.clear();

            // when
            Member findMember = em.find(Member.class, newMember.getId());
            Member refMember = em.getReference(Member.class, newMember.getId());

            System.out.println("refMember = " + refMember.getClass());
            System.out.println("findMember = " + findMember.getClass());

            // then
            assertThat(refMember).isSameAs(findMember);
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
