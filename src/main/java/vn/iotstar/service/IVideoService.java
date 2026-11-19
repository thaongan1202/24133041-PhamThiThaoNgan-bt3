package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.Video;

public interface IVideoService {
    void insert(Video video);
    void update(Video video);
    void delete(String videoId);
    Video findById(String videoId);
    List<Video> findAll(int page, int pageSize);
    List<Video> findActive(int page, int pageSize);
    List<Video> findLatest(int limit);
    int count();
    int countActive();
    void incrementViews(String videoId);
    
    List<Video> findByUser(int userId, int page, int pageSize);
    int countByUser(int userId);
}
