package com.devjoon.plugin.action;

import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.devjoon.plugin.commom.utils.HttpUtil.parseRequestBody;

public class ItemSearchAction extends BaseRestHandler {
    private static final Logger logger = Loggers.getLogger(ItemSearchAction.class);
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private static final String BASE_URI = "/_isearch/";

    @Override
    public String getName() {
        return "item_search_action";
    }

    @Override
    public List<Route> routes() {
        return List.of(
                new Route(RestRequest.Method.GET, BASE_URI),
                new Route(RestRequest.Method.POST, BASE_URI)
        );
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        var isSynonyms = request.paramAsBoolean("is-synonyms", false);
        var requestBody = parseRequestBody(request);
        logger.info("{}", requestBody.toString());
        return channel -> {
            final String action = request.path().replace("/_", "");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.source(searchSourceBuilder);
            client.search(searchRequest, new ActionListener<>() {
                @Override
                public void onResponse(SearchResponse searchResponse) {
                    channel.sendResponse(new BytesRestResponse(RestStatus.OK, CONTENT_TYPE_JSON, searchResponse.toString()));
                }

                @Override
                public void onFailure(Exception e) {
                    channel.sendResponse(new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, CONTENT_TYPE_JSON, e.toString()));
                }
            });

        };
    }


}


//return channel -> {
//			final String action = request.path().replace("/_","");
//			logger.info("action: {}", action);
//			final StringWriter buffer = new StringWriter();
//			JSONWriter builder = new JSONWriter(buffer);
//
//			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//			searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//			searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//			SearchRequest searchRequest = new SearchRequest();
//			searchRequest.source(searchSourceBuilder);
//client.search(searchRequest, new ActionListener<SearchResponse>() {
//					@Override
//					public void onResponse(SearchResponse searchResponse) {
//						logger.info("비동기 요청. 요청 횟수: {}", ++asyncCount);
//						builder.object()
//								.key("action").value(action)
//								.key("count").value(asyncCount)
//								.endObject();
//						channel.sendResponse(new BytesRestResponse(RestStatus.OK, CONTENT_TYPE_JSON, buffer.toString()));
//					}
//
//					@Override
//					public void onFailure(Exception e) {
//						logger.error("비동기 방식의 요청.  에러:", e);
//						builder.object()
//								.key("action").value(action)
//								.key("count").value(asyncCount)
//								.endObject();
//						channel.sendResponse(new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, CONTENT_TYPE_JSON, buffer.toString()));
//					}
//				});
//
//			}
//		};
//	}

///**
//	 * 각 노드에 신호 전파
//	 */
//	private void distribute(NodeClient client, String action, JSONObject body, boolean selfDist) {
//		String localNodeId = client.getLocalNodeId();
//		NodesInfoRequest infoRequest = new NodesInfoRequest();
//		infoRequest.clear()
//			.addMetrics(Metric.JVM.metricName())
//			.addMetrics(Metric.OS.metricName())
//			.addMetrics(Metric.PROCESS.metricName())
//			.addMetrics(Metric.HTTP.metricName())
//			.addMetrics(Metric.PLUGINS.metricName())
//			.addMetrics(Metric.INDICES.metricName());
//
//		try {
//			NodesInfoResponse response = client.admin().cluster().nodesInfo(infoRequest).get();
//			List<NodeInfo> nodes = response.getNodes();
//			for (NodeInfo node : nodes) {
//				// 자기자신이 아닌경우에만 전파
//				if (!selfDist && localNodeId.equals(node.getNode().getId())) {
//					continue;
//				}
//				boolean hasPlugin = false;
//				TransportAddress address = node.getInfo(HttpInfo.class).address().publishAddress();
//				// 상품명분석기 플러그인을 가진 노드에만 전파
//				List<PluginInfo> plugins = node.getInfo(PluginsAndModules.class).getPluginInfos();
//				for (PluginInfo info : plugins) {
//					if (hasPlugin = info.getClassname().equals(AnalysisProductNamePlugin.class.getName())) {
//						break;
//					}
//				}
//				if (hasPlugin) {
//					logger.debug("NODE: {}:{}", address.getAddress(), address.getPort());
//					doRestRequest(address.getAddress(), address.getPort(), action, body);
//				}
//			}
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//	}
//
//	/**
//	 * 특정 노드에 REST 신호를 보낸다
//	 */
//	private String doRestRequest(final String address, final int port, final String action, final JSONObject body) {
//		SpecialPermission.check();
//		return AccessController.doPrivileged((PrivilegedAction<String>) () -> {
//			StringBuilder sb = new StringBuilder();
//			HttpURLConnection con = null;
//			OutputStream ostream = null;
//			BufferedReader reader = null;
//			try {
//				String url = "http://" + address + ":" + port + BASE_URI + "/" + action;
//				logger.debug("SEND REQUEST {}", url);
//				con = (HttpURLConnection) new URL(url).openConnection();
//				con.setRequestMethod("POST");
//				con.setRequestProperty("Content-Type", "application/json");
//				con.setDoOutput(true);
//				ostream = con.getOutputStream();
//				ostream.write(String.valueOf(body).getBytes());
//				ostream.flush();
//				int responseCode = con.getResponseCode();
//				if (responseCode == HttpURLConnection.HTTP_CREATED) {
//					logger.trace("RESPONSE:{}", responseCode);
//					reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//					for (String rl; (rl = reader.readLine()) != null;) {
//						sb.append(rl).append("\r\n");
//					}
//					logger.trace("RESPONSE:{}", sb);
//				}
//			} catch (Exception e) {
//				logger.error("", e);
//			} finally {
//				try { ostream.close(); } catch (Exception ignore) { }
//				try { reader.close(); } catch (Exception ignore) { }
//			}
//			return String.valueOf(sb);
//		});
//	}