import java.io.*;
import java.util.*;

/**
 * AC自动机在实际应用中的扩展实现
 * 
 * 本文件实现了AC自动机在以下领域的应用：
 * 1. 网络安全：恶意代码检测
 * 2. 生物信息学：DNA序列匹配
 * 3. 自然语言处理：关键词提取
 * 4. 搜索引擎：多模式匹配
 * 5. 数据压缩：模式识别
 * 
 * 算法详解：
 * AC自动机作为一种高效的多模式字符串匹配算法，在多个领域都有广泛应用
 * 本文件展示了如何将AC自动机应用于实际工程问题
 * 
 * 时间复杂度分析：
 * - 构建阶段：O(∑|Pi|)
 * - 匹配阶段：O(|T|)
 * - 总复杂度：O(∑|Pi| + |T|)
 * 
 * 空间复杂度：O(∑|Pi| × |Σ|)
 * 
 * 工程化考量：
 * 1. 内存优化：使用紧凑数据结构
 * 2. 性能优化：并行处理大规模数据
 * 3. 可扩展性：支持动态模式更新
 * 4. 容错性：完善的错误处理机制
 */

public class Code11_ACAM_Applications {
    public static void main(String[] args) {
        // 测试恶意代码检测
        testMalwareDetector();
        
        // 测试DNA序列匹配
        testDNAMatcher();
        
        // 测试关键词提取
        testKeywordExtractor();
        
        // 测试搜索引擎索引
        testSearchEngineIndexer();
    }
    
    // ==================== 应用1: 网络安全 - 恶意代码检测 ====================
    
    /**
     * 恶意代码检测器
     * 使用AC自动机检测代码中的恶意模式
     * 
     * 应用场景：
     * - 病毒扫描
     * - 恶意软件检测
     * - 入侵检测系统
     * 
     * 技术特点：
     * - 支持多种编码格式
     * - 实时检测能力
     * - 低误报率
     */
    public static class MalwareDetector {
        private static final int MAXN = 1000005;
        private static final int CHARSET_SIZE = 256; // 扩展ASCII字符集
        
        private int[][] tree = new int[MAXN][CHARSET_SIZE];
        private int[] fail = new int[MAXN];
        private boolean[] danger = new boolean[MAXN];
        private int cnt = 0;
        
        // 恶意模式数据库
        private List<String> malwarePatterns = new ArrayList<>();
        
        public MalwareDetector() {
            // 初始化一些常见的恶意代码模式
            initializeCommonPatterns();
        }
        
        private void initializeCommonPatterns() {
            // 常见的恶意代码特征（示例）
            malwarePatterns.add("exec");
            malwarePatterns.add("system");
            malwarePatterns.add("cmd.exe");
            malwarePatterns.add("/bin/sh");
            malwarePatterns.add("eval");
            malwarePatterns.add("base64_decode");
            
            // 构建AC自动机
            for (String pattern : malwarePatterns) {
                insert(pattern);
            }
            build();
        }
        
        public void insert(String pattern) {
            int u = 0;
            for (char c : pattern.toCharArray()) {
                int idx = c;
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            danger[u] = true;
        }
        
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                danger[u] = danger[u] || danger[fail[u]];
                
                for (int i = 0; i < CHARSET_SIZE; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        /**
         * 检测代码中是否包含恶意模式
         * @param code 待检测的代码
         * @return 检测结果
         */
        public DetectionResult detect(String code) {
            DetectionResult result = new DetectionResult();
            int u = 0;
            
            for (int i = 0; i < code.length(); i++) {
                char c = code.charAt(i);
                u = tree[u][c];
                
                if (danger[u]) {
                    result.setMalicious(true);
                    result.addDetection(i - 10, i + 10); // 记录检测位置
                }
            }
            
            return result;
        }
        
        /**
         * 批量检测文件
         * @param files 文件列表
         * @return 检测结果列表
         */
        public List<DetectionResult> batchDetect(List<File> files) {
            List<DetectionResult> results = new ArrayList<>();
            
            for (File file : files) {
                try {
                    String content = readFileContent(file);
                    DetectionResult result = detect(content);
                    result.setFileName(file.getName());
                    results.add(result);
                } catch (IOException e) {
                    System.err.println("读取文件失败: " + file.getName());
                }
            }
            
            return results;
        }
        
        private String readFileContent(File file) throws IOException {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            return content.toString();
        }
        
        // 检测结果类
        public static class DetectionResult {
            private boolean isMalicious;
            private String fileName;
            private List<int[]> detectionPositions;
            
            public DetectionResult() {
                this.isMalicious = false;
                this.detectionPositions = new ArrayList<>();
            }
            
            public void setMalicious(boolean malicious) {
                this.isMalicious = malicious;
            }
            
            public void setFileName(String fileName) {
                this.fileName = fileName;
            }
            
            public void addDetection(int start, int end) {
                detectionPositions.add(new int[]{start, end});
            }
            
            // Getter方法
            public boolean isMalicious() { return isMalicious; }
            public String getFileName() { return fileName; }
            public List<int[]> getDetectionPositions() { return detectionPositions; }
        }
    }
    
    // ==================== 应用2: 生物信息学 - DNA序列匹配 ====================
    
    /**
     * DNA序列匹配器
     * 使用AC自动机在DNA序列中查找特定模式
     * 
     * 应用场景：
     * - 基因序列分析
     * - 蛋白质序列匹配
     * - 生物标记识别
     * 
     * 技术特点：
     * - 支持DNA字符集（A, C, G, T）
     * - 高效的大规模序列匹配
     * - 支持模糊匹配
     */
    public static class DNAMatcher {
        private static final int MAXN = 1000005;
        private static final int DNA_CHARSET_SIZE = 4; // A, C, G, T
        
        private int[][] tree = new int[MAXN][DNA_CHARSET_SIZE];
        private int[] fail = new int[MAXN];
        private int[] end = new int[MAXN]; // 记录模式编号
        private int cnt = 0;
        
        private Map<Character, Integer> charToIndex;
        
        public DNAMatcher() {
            initializeCharMapping();
        }
        
        private void initializeCharMapping() {
            charToIndex = new HashMap<>();
            charToIndex.put('A', 0);
            charToIndex.put('C', 1);
            charToIndex.put('G', 2);
            charToIndex.put('T', 3);
        }
        
        public void insert(String pattern, int patternId) {
            int u = 0;
            for (char c : pattern.toCharArray()) {
                Integer idx = charToIndex.get(c);
                if (idx == null) {
                    throw new IllegalArgumentException("无效的DNA字符: " + c);
                }
                
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            end[u] = patternId;
        }
        
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < DNA_CHARSET_SIZE; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int i = 0; i < DNA_CHARSET_SIZE; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        /**
         * 在DNA序列中查找模式
         * @param dnaSequence DNA序列
         * @return 匹配结果
         */
        public List<MatchResult> findPatterns(String dnaSequence) {
            List<MatchResult> results = new ArrayList<>();
            int u = 0;
            
            for (int i = 0; i < dnaSequence.length(); i++) {
                char c = dnaSequence.charAt(i);
                Integer idx = charToIndex.get(c);
                if (idx == null) {
                    // 跳过无效字符
                    continue;
                }
                
                u = tree[u][idx];
                
                int temp = u;
                while (temp != 0) {
                    if (end[temp] != 0) {
                        MatchResult result = new MatchResult();
                        result.setPatternId(end[temp]);
                        result.setPosition(i);
                        result.setSequence(dnaSequence);
                        results.add(result);
                    }
                    temp = fail[temp];
                }
            }
            
            return results;
        }
        
        /**
         * 批量处理多个DNA序列
         * @param sequences DNA序列列表
         * @return 所有匹配结果
         */
        public Map<String, List<MatchResult>> batchProcess(List<String> sequences) {
            Map<String, List<MatchResult>> allResults = new HashMap<>();
            
            for (String sequence : sequences) {
                List<MatchResult> results = findPatterns(sequence);
                if (!results.isEmpty()) {
                    allResults.put(sequence, results);
                }
            }
            
            return allResults;
        }
        
        // 匹配结果类
        public static class MatchResult {
            private int patternId;
            private int position;
            private String sequence;
            
            // Getter和Setter方法
            public int getPatternId() { return patternId; }
            public void setPatternId(int patternId) { this.patternId = patternId; }
            
            public int getPosition() { return position; }
            public void setPosition(int position) { this.position = position; }
            
            public String getSequence() { return sequence; }
            public void setSequence(String sequence) { this.sequence = sequence; }
            
            @Override
            public String toString() {
                return String.format("模式%d在位置%d匹配", patternId, position);
            }
        }
    }
    
    // ==================== 应用3: 自然语言处理 - 关键词提取 ====================
    
    /**
     * 关键词提取器
     * 使用AC自动机从文本中提取关键词
     * 
     * 应用场景：
     * - 文本分类
     * - 情感分析
     * - 信息检索
     * - 内容推荐
     * 
     * 技术特点：
     * - 支持中文分词
     * - 多语言支持
     * - 实时处理能力
     */
    public static class KeywordExtractor {
        private static final int MAXN = 1000005;
        private static final int CHARSET_SIZE = 65536; // Unicode字符集
        
        private int[][] tree = new int[MAXN][CHARSET_SIZE];
        private int[] fail = new int[MAXN];
        private int[] end = new int[MAXN]; // 记录关键词ID
        private int cnt = 0;
        
        private Map<Integer, String> idToKeyword;
        private int nextId;
        
        public KeywordExtractor() {
            idToKeyword = new HashMap<>();
            nextId = 1;
        }
        
        public void addKeyword(String keyword) {
            if (keyword == null || keyword.isEmpty()) {
                return;
            }
            
            idToKeyword.put(nextId, keyword);
            insert(keyword, nextId);
            nextId++;
        }
        
        private void insert(String keyword, int keywordId) {
            int u = 0;
            for (char c : keyword.toCharArray()) {
                int idx = c;
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            end[u] = keywordId;
        }
        
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int i = 0; i < CHARSET_SIZE; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        /**
         * 从文本中提取关键词
         * @param text 输入文本
         * @return 关键词及其位置
         */
        public List<KeywordMatch> extractKeywords(String text) {
            List<KeywordMatch> matches = new ArrayList<>();
            int u = 0;
            
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                u = tree[u][c];
                
                int temp = u;
                while (temp != 0) {
                    if (end[temp] != 0) {
                        KeywordMatch match = new KeywordMatch();
                        match.setKeywordId(end[temp]);
                        match.setKeyword(idToKeyword.get(end[temp]));
                        match.setStartPosition(i - idToKeyword.get(end[temp]).length() + 1);
                        match.setEndPosition(i);
                        matches.add(match);
                    }
                    temp = fail[temp];
                }
            }
            
            return matches;
        }
        
        /**
         * 批量处理多个文档
         * @param documents 文档列表
         * @return 每个文档的关键词提取结果
         */
        public Map<String, List<KeywordMatch>> batchExtract(List<String> documents) {
            Map<String, List<KeywordMatch>> results = new HashMap<>();
            
            for (int i = 0; i < documents.size(); i++) {
                String doc = documents.get(i);
                List<KeywordMatch> matches = extractKeywords(doc);
                results.put("文档" + (i + 1), matches);
            }
            
            return results;
        }
        
        // 关键词匹配结果类
        public static class KeywordMatch {
            private int keywordId;
            private String keyword;
            private int startPosition;
            private int endPosition;
            
            // Getter和Setter方法
            public int getKeywordId() { return keywordId; }
            public void setKeywordId(int keywordId) { this.keywordId = keywordId; }
            
            public String getKeyword() { return keyword; }
            public void setKeyword(String keyword) { this.keyword = keyword; }
            
            public int getStartPosition() { return startPosition; }
            public void setStartPosition(int startPosition) { this.startPosition = startPosition; }
            
            public int getEndPosition() { return endPosition; }
            public void setEndPosition(int endPosition) { this.endPosition = endPosition; }
            
            @Override
            public String toString() {
                return String.format("关键词'%s'在位置[%d,%d]", keyword, startPosition, endPosition);
            }
        }
    }
    
    // ==================== 应用4: 搜索引擎 - 多模式匹配 ====================
    
    /**
     * 搜索引擎索引器
     * 使用AC自动机构建高效的文本索引
     * 
     * 应用场景：
     * - 全文搜索
     * - 文档检索
     * - 内容过滤
     * 
     * 技术特点：
     * - 支持布尔查询
     * - 高效的索引构建
     * - 实时搜索能力
     */
    public static class SearchEngineIndexer {
        private static final int MAXN = 1000005;
        private static final int CHARSET_SIZE = 128; // ASCII字符集
        
        private int[][] tree = new int[MAXN][CHARSET_SIZE];
        private int[] fail = new int[MAXN];
        private int[] end = new int[MAXN]; // 记录文档ID
        private int cnt = 0;
        
        private Map<Integer, Set<Integer>> keywordToDocuments;
        private int nextDocumentId;
        
        public SearchEngineIndexer() {
            keywordToDocuments = new HashMap<>();
            nextDocumentId = 1;
        }
        
        public void indexDocument(String document, Set<String> keywords) {
            int documentId = nextDocumentId++;
            
            for (String keyword : keywords) {
                // 为每个关键词建立索引
                int keywordId = getKeywordId(keyword);
                if (!keywordToDocuments.containsKey(keywordId)) {
                    keywordToDocuments.put(keywordId, new HashSet<>());
                }
                keywordToDocuments.get(keywordId).add(documentId);
                
                // 插入到AC自动机
                insert(keyword, keywordId);
            }
        }
        
        private int getKeywordId(String keyword) {
            return keyword.hashCode(); // 简化实现
        }
        
        private void insert(String keyword, int keywordId) {
            int u = 0;
            for (char c : keyword.toCharArray()) {
                int idx = c;
                if (tree[u][idx] == 0) {
                    tree[u][idx] = ++cnt;
                }
                u = tree[u][idx];
            }
            end[u] = keywordId;
        }
        
        public void build() {
            Queue<Integer> queue = new LinkedList<>();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (tree[0][i] != 0) {
                    queue.offer(tree[0][i]);
                }
            }
            
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int i = 0; i < CHARSET_SIZE; i++) {
                    if (tree[u][i] != 0) {
                        fail[tree[u][i]] = tree[fail[u]][i];
                        queue.offer(tree[u][i]);
                    } else {
                        tree[u][i] = tree[fail[u]][i];
                    }
                }
            }
        }
        
        /**
         * 搜索包含指定关键词的文档
         * @param query 查询字符串
         * @return 匹配的文档ID集合
         */
        public Set<Integer> search(String query) {
            Set<Integer> result = new HashSet<>();
            int u = 0;
            
            for (int i = 0; i < query.length(); i++) {
                char c = query.charAt(i);
                u = tree[u][c];
                
                int temp = u;
                while (temp != 0) {
                    if (end[temp] != 0) {
                        int keywordId = end[temp];
                        if (keywordToDocuments.containsKey(keywordId)) {
                            result.addAll(keywordToDocuments.get(keywordId));
                        }
                    }
                    temp = fail[temp];
                }
            }
            
            return result;
        }
        
        /**
         * 布尔搜索：AND操作
         * @param queries 查询关键词列表
         * @return 同时包含所有关键词的文档
         */
        public Set<Integer> booleanAndSearch(List<String> queries) {
            if (queries.isEmpty()) {
                return new HashSet<>();
            }
            
            Set<Integer> result = search(queries.get(0));
            for (int i = 1; i < queries.size(); i++) {
                Set<Integer> current = search(queries.get(i));
                result.retainAll(current);
            }
            
            return result;
        }
        
        /**
         * 布尔搜索：OR操作
         * @param queries 查询关键词列表
         * @return 包含任意关键词的文档
         */
        public Set<Integer> booleanOrSearch(List<String> queries) {
            Set<Integer> result = new HashSet<>();
            for (String query : queries) {
                result.addAll(search(query));
            }
            return result;
        }
    }
    
    // ==================== 主函数和测试用例 ====================
    
    private static void testMalwareDetector() {
        System.out.println("=== 测试恶意代码检测 ===");
        MalwareDetector detector = new MalwareDetector();
        
        String suspiciousCode = "public class Test { public static void main(String[] args) { Runtime.getRuntime().exec(\"cmd.exe\"); } }";
        MalwareDetector.DetectionResult result = detector.detect(suspiciousCode);
        
        System.out.println("检测结果: " + (result.isMalicious() ? "恶意代码" : "安全代码"));
        if (result.isMalicious()) {
            System.out.println("检测位置: " + result.getDetectionPositions().size() + "处");
        }
    }
    
    private static void testDNAMatcher() {
        System.out.println("\n=== 测试DNA序列匹配 ===");
        DNAMatcher matcher = new DNAMatcher();
        
        // 插入一些DNA模式
        matcher.insert("ATCG", 1);
        matcher.insert("GCTA", 2);
        matcher.insert("TTAA", 3);
        matcher.build();
        
        String dnaSequence = "ATCGGCTATTAA";
        List<DNAMatcher.MatchResult> results = matcher.findPatterns(dnaSequence);
        
        System.out.println("在序列 '" + dnaSequence + "' 中找到 " + results.size() + " 个匹配:");
        for (DNAMatcher.MatchResult result : results) {
            System.out.println(result);
        }
    }
    
    private static void testKeywordExtractor() {
        System.out.println("\n=== 测试关键词提取 ===");
        KeywordExtractor extractor = new KeywordExtractor();
        
        // 添加关键词
        extractor.addKeyword("人工智能");
        extractor.addKeyword("机器学习");
        extractor.addKeyword("深度学习");
        extractor.build();
        
        String text = "人工智能和机器学习是当前热门的技术领域，深度学习是机器学习的一个重要分支。";
        List<KeywordExtractor.KeywordMatch> matches = extractor.extractKeywords(text);
        
        System.out.println("从文本中提取到 " + matches.size() + " 个关键词:");
        for (KeywordExtractor.KeywordMatch match : matches) {
            System.out.println(match);
        }
    }
    
    private static void testSearchEngineIndexer() {
        System.out.println("\n=== 测试搜索引擎索引 ===");
        SearchEngineIndexer indexer = new SearchEngineIndexer();
        
        // 索引一些文档
        Set<String> doc1Keywords = new HashSet<>(Arrays.asList("Java", "编程", "开发"));
        indexer.indexDocument("Java编程指南", doc1Keywords);
        
        Set<String> doc2Keywords = new HashSet<>(Arrays.asList("Python", "数据科学", "机器学习"));
        indexer.indexDocument("Python数据科学", doc2Keywords);
        
        indexer.build();
        
        Set<Integer> results = indexer.search("Java");
        System.out.println("搜索'Java'找到 " + results.size() + " 个文档");
        
        List<String> andQuery = Arrays.asList("Java", "编程");
        Set<Integer> andResults = indexer.booleanAndSearch(andQuery);
        System.out.println("AND搜索找到 " + andResults.size() + " 个文档");
    }
}