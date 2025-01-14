package com.book.jpa.learn;

import com.book.jpa.domain.member.Member;
import jakarta.persistence.*;
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

            member.setUserName("김길동");
            member.setUserTelNo("01098765432");

            em.flush();

            et.commit();

            Member findMember = em.find(Member.class, member.getId());

            assertThat(findMember.getUserName()).isEqualTo("김길동");
            assertThat(findMember.getUserTelNo()).isEqualTo("01098765432");

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Test
    @DisplayName("쓰기 지연 확인")
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
}
