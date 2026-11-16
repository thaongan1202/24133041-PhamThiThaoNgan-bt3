package vn.iotstar.service.impl;

import java.util.List;

import vn.iotstar.dao.IVideoDao;
import vn.iotstar.dao.VideoDao;
import vn.iotstar.entity.Video;
import vn.iotstar.service.IVideoService;

public class VideoServiceImpl implements IVideoService {
    private final IVideoDao videoDao = new VideoDao();

    @Override
    public void insert(Video video) {
        videoDao.insert(video);
    }

    @Override
    public void update(Video video) {
        videoDao.update(video);
    }

    @Override
    public void delete(String videoId) {
        videoDao.delete(videoId);
    }

    @Override
    public Video findById(String videoId) {
        return videoDao.findById(videoId);
    }

    @Override
    public List<Video> findAll(int page, int pageSize) {
        return videoDao.findAll(page, pageSize);
    }

    @Override
    public List<Video> findActive(int page, int pageSize) {
        return videoDao.findActive(page, pageSize);
    }

    @Override
    public List<Video> findLatest(int limit) {
        return videoDao.findLatest(limit);
    }

    @Override
    public int count() {
        return videoDao.count();
    }

    @Override
    public int countActive() {
        return videoDao.countActive();
    }

    @Override
    public void incrementViews(String videoId) {
        videoDao.incrementViews(videoId);
    }
}
