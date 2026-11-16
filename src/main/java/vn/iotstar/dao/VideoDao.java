package vn.iotstar.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;

public class VideoDao implements IVideoDao {
    @Override
    public void insert(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            attachReferences(em, video);
            em.persist(video);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            attachReferences(em, video);
            em.merge(video);
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(String videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Video video = em.find(Video.class, videoId);
            if (video != null) {
                em.remove(video);
            }
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Video findById(String videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Video.class, videoId);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findAll(int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Video> query = em.createQuery(
                    "SELECT v FROM Video v ORDER BY v.createdDate DESC, v.videoId DESC", Video.class);
            query.setFirstResult(Math.max(0, page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findActive(int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Video> query = em.createQuery(
                    "SELECT v FROM Video v WHERE v.active = 1 ORDER BY v.createdDate DESC, v.videoId DESC",
                    Video.class);
            query.setFirstResult(Math.max(0, page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findLatest(int limit) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Video> query = em.createQuery(
                    "SELECT v FROM Video v WHERE v.active = 1 ORDER BY v.createdDate DESC, v.videoId DESC",
                    Video.class);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int count() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(v) FROM Video v", Long.class).getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public int countActive() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(v) FROM Video v WHERE v.active = 1", Long.class)
                    .getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public void incrementViews(String videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createQuery("UPDATE Video v SET v.views = v.views + 1 WHERE v.videoId = :id")
                    .setParameter("id", videoId)
                    .executeUpdate();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private void attachReferences(EntityManager em, Video video) {
        if (video.getCategory() != null) {
            video.setCategory(em.getReference(Category.class, video.getCategory().getCategoryid()));
        }
        if (video.getUser() != null) {
            video.setUser(em.getReference(User.class, video.getUser().getId()));
        }
    }
}
