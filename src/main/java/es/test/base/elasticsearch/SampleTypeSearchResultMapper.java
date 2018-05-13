package es.test.base.elasticsearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.ElasticsearchException;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.ScriptedField;
import org.springframework.data.elasticsearch.core.AbstractResultMapper;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.mapping.context.MappingContext;

import es.test.base.utils.JsonUtils;
import es.test.base.utils.ReflectionUtil;

/**
 * 将type放入查询结果
 *
 * @author 旺旺小学酥
 */
public class SampleTypeSearchResultMapper extends AbstractResultMapper {

    private MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty> mappingContext;

    public SampleTypeSearchResultMapper() {
        super(new DefaultEntityMapper());
    }

    public SampleTypeSearchResultMapper(
        final MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty>
            mappingContext) {
        super(new DefaultEntityMapper());
        this.mappingContext = mappingContext;
    }

    public SampleTypeSearchResultMapper(final EntityMapper entityMapper) {
        super(entityMapper);
    }

    public SampleTypeSearchResultMapper(
        final MappingContext<? extends ElasticsearchPersistentEntity<?>, ElasticsearchPersistentProperty>
            mappingContext,
        final EntityMapper entityMapper) {
        super(entityMapper);
        this.mappingContext = mappingContext;
    }

    public <T> AggregatedPage<T> mapResults(final SearchResponse response, final Class<T> clazz,
        final Pageable pageable) {
        final long totalHits = response.getHits().totalHits();
        final List<T> results = new ArrayList<T>();
        for (final SearchHit hit : response.getHits()) {
            if (hit != null) {
                T result = null;
                if (StringUtils.isNotBlank(hit.sourceAsString())) {
                    result = mapEntity(hit.sourceAsString(), clazz);
                } else {
                    result = mapEntity(hit.getFields().values(), clazz);
                }
                setPersistentEntityId(result, hit.getId(), clazz);
                populateScriptFields(result, hit);
                ReflectionUtil.setFieldValue(result, "type", hit.getType());
                //                ReflectionUtil.setFieldValue(result, "score", hit.getScore());
                results.add(result);
            }
        }

        return new AggregatedPageImpl<T>(results, pageable, totalHits, response.getAggregations());
    }

    private <T> T mapEntity(final Collection<SearchHitField> values, final Class<T> clazz) {
        return mapEntity(JsonUtils.toJson(values), clazz);
    }

    private <T> void setPersistentEntityId(final T result, final String id, final Class<T> clazz) {

        if (this.mappingContext != null && clazz.isAnnotationPresent(Document.class)) {

            final ElasticsearchPersistentEntity<?> persistentEntity = this.mappingContext.getPersistentEntity(clazz);
            final PersistentProperty<?> idProperty = persistentEntity.getIdProperty();

            // Only deal with String because ES generated Ids are strings !
            if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
                persistentEntity.getPropertyAccessor(result).setProperty(idProperty, id);
            }
        }
    }

    private <T> void populateScriptFields(final T result, final SearchHit hit) {
        if (hit.getFields() != null && !hit.getFields().isEmpty() && result != null) {
            for (final java.lang.reflect.Field field : result.getClass().getDeclaredFields()) {
                final ScriptedField scriptedField = field.getAnnotation(ScriptedField.class);
                if (scriptedField != null) {
                    final String name = scriptedField.name().isEmpty() ? field.getName() : scriptedField.name();
                    final SearchHitField searchHitField = hit.getFields().get(name);
                    if (searchHitField != null) {
                        field.setAccessible(true);
                        try {
                            field.set(result, searchHitField.getValue());
                        } catch (final IllegalArgumentException e) {
                            throw new ElasticsearchException(
                                "failed to set scripted field: " + name + " with value: " + searchHitField.getValue(),
                                e);
                        } catch (final IllegalAccessException e) {
                            throw new ElasticsearchException("failed to access scripted field: " + name, e);
                        }
                    }
                }
            }
        }
    }

    public <T> T mapResult(final GetResponse response, final Class<T> clazz) {
        final T result = mapEntity(response.getSourceAsString(), clazz);
        if (result != null) {
            setPersistentEntityId(result, response.getId(), clazz);
        }
        return result;
    }

    public <T> LinkedList<T> mapResults(final MultiGetResponse responses, final Class<T> clazz) {
        final LinkedList<T> list = new LinkedList<T>();
        for (final MultiGetItemResponse response : responses.getResponses()) {
            if (!response.isFailed() && response.getResponse().isExists()) {
                final T result = mapEntity(response.getResponse().getSourceAsString(), clazz);
                setPersistentEntityId(result, response.getResponse().getId(), clazz);
                list.add(result);
            }
        }
        return list;
    }
}
