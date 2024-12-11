INSERT INTO tb_campaign_info(
    campaign_id,campaign_category, campaign_status, campaign_platform, campaign_title, request_company,create_at,update_at
)values
(1, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(2, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(3, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(4, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(5, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(6, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(7, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(8, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(9, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(10, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(11, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(12, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(13, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(14, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(15, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(16, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(17, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(18, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(19, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null),
(20, 'DAILY', 'PROGRESS', 'NAVER','타이틀','몽테르',now(),null);

INSERT INTO tb_campaign_details(
    campaign_details_id,
    campaign_id,
    campaign_img_url,
    campaign_price,
    campaign_total_price,
    campaign_url,
    active_count,
    active_date,
    finish_date,
    option_count,
    total_count,
    review_point,
    daily_count,
    start_time,
    end_time,
    seller_request,
    create_at,
    update_at
)values
(1,1,'IMAGE_URL',100,1000,'URL',)