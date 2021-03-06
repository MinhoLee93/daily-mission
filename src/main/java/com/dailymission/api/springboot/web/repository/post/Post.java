package com.dailymission.api.springboot.web.repository.post;

import com.dailymission.api.springboot.web.repository.common.BaseTimeEntity;
import com.dailymission.api.springboot.web.repository.mission.Mission;
import com.dailymission.api.springboot.web.repository.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MISSION_ID", referencedColumnName = "id",  nullable = false)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "TITLE", length = 500, nullable = false)
    private String title;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "ORIGINAL_FILE_NAME", nullable = false)
    private String originalFileName;

    @Column(name = "FILE_EXTENSION", nullable = false)
    private String fileExtension;

    @Column(name="IMAGE_URL", nullable = false, length = 2000)
    private String imageUrl;

    @Column(name="THUMBNAIL_URL", nullable = false, length = 2000)
    private String thumbnailUrl;

    @Column(name="THUMBNAIL_URL_MISSION", nullable = false, length = 2000)
    private String thumbnailUrlMission;

    @Column(name="THUMBNAIL_URL_MY", nullable = false, length = 2000)
    private String thumbnailUrlMy;

    @Column(name = "DELETED")
    private boolean deleted;

    @Builder
    public Post(Mission mission, User user, String title, String content
                , String originalFileName, String fileExtension, String imageUrl){
        this.mission = mission;
        this.user = user;
        this.title = title;
        this.content = content;
        this.originalFileName = originalFileName;
        this.fileExtension = fileExtension;

        // s3
        this.imageUrl = imageUrl;
        this.thumbnailUrl = imageUrl;
        this.thumbnailUrlMy = imageUrl;
        this.thumbnailUrlMission = imageUrl;

        this.deleted = false;
    }

//    // 내용 업데이트
//    public void update(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
//
    // 이미지 업데이트
    public void updateImage(String imageUrl){
        // 이미지
        this.imageUrl = imageUrl;
        // 썸네일 -> 재생성
        this.thumbnailUrl = imageUrl;
        this.thumbnailUrlMy = imageUrl;
        this.thumbnailUrlMission = imageUrl;
    }

    // 썸네일 업데이트 (포스트 목록)
    public void updateThumbnail(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }

    // 썸네일 업데이트 (My)
    public void updateThumbnailMy(String thumbnailUrlMy){
        this.thumbnailUrlMy = thumbnailUrlMy;
    }

    // 썸네일 업데이트 (Mission)
    public void updateThumbnailMission(String thumbnailUrlMission){
        this.thumbnailUrlMission = thumbnailUrlMission;
    }

    // 삭제 가능한지 확인
    public boolean isDeletable(User user){

        // check user
        if(this.user != user){
            throw new IllegalArgumentException("허용되지 않은 유저입니다.");
        }

        // check post status
        if(this.deleted){
            throw new IllegalArgumentException("이미 삭제된 게시글입니다.");
        }

        return true;
    }

    // 삭제
    public void delete(){

        this.deleted = true;

    }
}
