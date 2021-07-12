package com.eden.elasticsearchsync.persistence.es.dao.repository;

import com.alibaba.fastjson.JSON;
import com.eden.elasticsearchsync.persistence.es.config.ElasticsearchConfig;
import com.eden.elasticsearchsync.persistence.es.dao.po.BaseEsPo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author : Eden
 * @date : 2021/7/11
 */
@Slf4j
@Getter
public abstract class BaseDao {

    @Resource
    protected RestHighLevelClient restHighLevelClient;

    protected static final Integer MAX_BULK_SIZE = 100;


    public <T extends BaseEsPo> Optional<T> findByUserId(Long userId, Class<T> clazz) {
        String className = this.getClass().getSimpleName();
        List<T> pos = new ArrayList<>();
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(QueryBuilders.matchPhraseQuery("_id", userId));
        request.source(searchSourceBuilder);
        request.indices(ElasticsearchConfig.ES_INDEX_NAME);
        request.types(ElasticsearchConfig.ES_TYPE_NAME);
        try {
            log.info("className = {}, restHighLevelClient.search, req = {}", className, JSON.toJSONString(request));
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            log.info("className = {}, restHighLevelClient.search, res = {}", className, JSON.toJSONString(response));
            for (SearchHit hit : response.getHits().getHits()) {
                String id = hit.getId();
                T po = JSON.parseObject(hit.getSourceAsString(), clazz);
                po.setEsId(id);
                pos.add(po);
            }
        } catch (IOException e) {
            log.error(String.format("className = %s, es查询异常, userId = %s", className, userId), e);
            throw new RuntimeException(e);
        }
        if (CollectionUtils.isEmpty(pos)) {
            return Optional.empty();
        }
        if (CollectionUtils.size(pos) > 1) {
            log.info("es:存在多条数据:userId:{}", userId);
        }
        return Optional.of(pos.get(0));
    }


    public <T extends BaseEsPo> void insertOrUpdate(T po, Class<T> clazz) {
        String className = this.getClass().getSimpleName();
        log.info("className = {}, insertOrUpdate po = {}", className, JSON.toJSONString(po));
        po.checkElseThrow();
        Optional<T> optional = findByUserId(po.getUserId(), clazz);
        if (optional.isPresent()) {
            T poInEs = optional.get();
            log.info("className = {}, userId = {}, esId = {}", className, po.getUserId(), poInEs.getEsId());
            UpdateRequest request = new UpdateRequest(ElasticsearchConfig.ES_INDEX_NAME, ElasticsearchConfig.ES_TYPE_NAME, poInEs.getEsId());
            request.doc(JSON.toJSONString(po), XContentType.JSON);
            request.fetchSource(true);
            UpdateResponse response;
            try {
                log.info("className = {}, restHighLevelClient.update req = {}", className, JSON.toJSONString(request));
                response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
                log.info("className = {}, restHighLevelClient.update res = {}", className, JSON.toJSONString(response));
            } catch (ElasticsearchException | IOException e) {
                log.info("className = {}, 同步es失败, userId = {}", className, po.getUserId());
                throw new RuntimeException(e);
            }
            if (response != null) {
                if (response.getResult() == DocWriteResponse.Result.CREATED) {
                    log.info("className = {}, 新增文档成功, userId = {}", className, po.getUserId());
                } else if (response.getResult() == DocWriteResponse.Result.UPDATED) {
                    log.info("className = {}, 修改文档成功, userId = {}", className, po.getUserId());
                }
            }
        } else {
            String esId = String.valueOf(po.getUserId());
            IndexRequest request = new IndexRequest(ElasticsearchConfig.ES_INDEX_NAME, ElasticsearchConfig.ES_TYPE_NAME, esId);
            request.source(JSON.toJSONString(po), XContentType.JSON);
            IndexResponse response;
            try {
                log.info("className = {}, restHighLevelClient.index req = {}", className, JSON.toJSONString(request));
                response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
                log.info("className = {}, restHighLevelClient.index res = {}", className, JSON.toJSONString(response));
            } catch (ElasticsearchException | IOException e) {
                log.info("className = {}, 同步es失败, userId = {}", className, po.getUserId());
                throw new RuntimeException(e);
            }
            if (response != null) {
                if (response.getResult() == DocWriteResponse.Result.CREATED) {
                    log.info("className = {}, 新增文档成功, userId = {}", className, po.getUserId());
                } else if (response.getResult() == DocWriteResponse.Result.UPDATED) {
                    log.info("className = {}, 修改文档成功, userId = {}", className, po.getUserId());
                }
            }
        }
    }

}
