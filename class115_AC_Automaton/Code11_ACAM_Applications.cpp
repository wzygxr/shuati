#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <cstring>
#include <algorithm>
#include <unordered_map>
#include <unordered_set>
#include <fstream>
#include <filesystem>

/**
 * AC自动机在实际应用中的扩展实现 - C++版本
 * 
 * 本文件实现了AC自动机在以下领域的应用：
 * 1. 网络安全：恶意代码检测
 * 2. 生物信息学：DNA序列匹配
 * 3. 自然语言处理：关键词提取
 * 4. 搜索引擎：多模式匹配
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
 */

// ==================== 应用1: 网络安全 - 恶意代码检测 ====================

class MalwareDetector {
private:
    static const int MAXN = 1000005;
    static const int CHARSET_SIZE = 256;
    
    int tree[MAXN][CHARSET_SIZE];
    int fail[MAXN];
    bool danger[MAXN];
    int cnt;
    
    std::vector<std::string> malwarePatterns;
    
public:
    MalwareDetector() : cnt(0) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(danger, false, sizeof(danger));
        initializeCommonPatterns();
    }
    
private:
    void initializeCommonPatterns() {
        malwarePatterns = {
            "exec", "system", "cmd.exe", "/bin/sh", "eval", "base64_decode"
        };
        
        for (const auto& pattern : malwarePatterns) {
            insert(pattern);
        }
        build();
    }
    
    void insert(const std::string& pattern) {
        int u = 0;
        for (char c : pattern) {
            int idx = static_cast<int>(c);
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        danger[u] = true;
    }
    
    void build() {
        std::queue<int> q;
        for (int i = 0; i < CHARSET_SIZE; i++) {
            if (tree[0][i] != 0) {
                q.push(tree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            danger[u] = danger[u] || danger[fail[u]];
            
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (tree[u][i] != 0) {
                    fail[tree[u][i]] = tree[fail[u]][i];
                    q.push(tree[u][i]);
                } else {
                    tree[u][i] = tree[fail[u]][i];
                }
            }
        }
    }
    
public:
    struct DetectionResult {
        bool isMalicious;
        std::string fileName;
        std::vector<std::pair<int, int>> detectionPositions;
        
        DetectionResult() : isMalicious(false) {}
    };
    
    DetectionResult detect(const std::string& code) {
        DetectionResult result;
        int u = 0;
        
        for (int i = 0; i < code.length(); i++) {
            char c = code[i];
            u = tree[u][static_cast<int>(c)];
            
            if (danger[u]) {
                result.isMalicious = true;
                result.detectionPositions.push_back({i - 10, i + 10});
            }
        }
        
        return result;
    }
    
    std::vector<DetectionResult> batchDetect(const std::vector<std::string>& files) {
        std::vector<DetectionResult> results;
        
        for (const auto& file : files) {
            try {
                std::string content = readFileContent(file);
                DetectionResult result = detect(content);
                result.fileName = file;
                results.push_back(result);
            } catch (const std::exception& e) {
                std::cerr << "读取文件失败: " << file << std::endl;
            }
        }
        
        return results;
    }
    
private:
    std::string readFileContent(const std::string& filename) {
        std::ifstream file(filename);
        if (!file.is_open()) {
            throw std::runtime_error("无法打开文件: " + filename);
        }
        
        std::string content((std::istreambuf_iterator<char>(file)),
                           std::istreambuf_iterator<char>());
        return content;
    }
};

// ==================== 应用2: 生物信息学 - DNA序列匹配 ====================

class DNAMatcher {
private:
    static const int MAXN = 1000005;
    static const int DNA_CHARSET_SIZE = 4;
    
    int tree[MAXN][DNA_CHARSET_SIZE];
    int fail[MAXN];
    int end[MAXN];
    int cnt;
    
    std::unordered_map<char, int> charToIndex;
    
public:
    DNAMatcher() : cnt(0) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(end, 0, sizeof(end));
        initializeCharMapping();
    }
    
private:
    void initializeCharMapping() {
        charToIndex = {
            {'A', 0}, {'C', 1}, {'G', 2}, {'T', 3}
        };
    }
    
public:
    void insert(const std::string& pattern, int patternId) {
        int u = 0;
        for (char c : pattern) {
            auto it = charToIndex.find(c);
            if (it == charToIndex.end()) {
                throw std::invalid_argument("无效的DNA字符: " + std::string(1, c));
            }
            
            int idx = it->second;
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        end[u] = patternId;
    }
    
    void build() {
        std::queue<int> q;
        for (int i = 0; i < DNA_CHARSET_SIZE; i++) {
            if (tree[0][i] != 0) {
                q.push(tree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            for (int i = 0; i < DNA_CHARSET_SIZE; i++) {
                if (tree[u][i] != 0) {
                    fail[tree[u][i]] = tree[fail[u]][i];
                    q.push(tree[u][i]);
                } else {
                    tree[u][i] = tree[fail[u]][i];
                }
            }
        }
    }
    
    struct MatchResult {
        int patternId;
        int position;
        std::string sequence;
    };
    
    std::vector<MatchResult> findPatterns(const std::string& dnaSequence) {
        std::vector<MatchResult> results;
        int u = 0;
        
        for (int i = 0; i < dnaSequence.length(); i++) {
            char c = dnaSequence[i];
            auto it = charToIndex.find(c);
            if (it == charToIndex.end()) {
                continue;
            }
            
            int idx = it->second;
            u = tree[u][idx];
            
            int temp = u;
            while (temp != 0) {
                if (end[temp] != 0) {
                    MatchResult result;
                    result.patternId = end[temp];
                    result.position = i;
                    result.sequence = dnaSequence;
                    results.push_back(result);
                }
                temp = fail[temp];
            }
        }
        
        return results;
    }
};

// ==================== 应用3: 自然语言处理 - 关键词提取 ====================

class KeywordExtractor {
private:
    static const int MAXN = 1000005;
    static const int CHARSET_SIZE = 65536;
    
    int tree[MAXN][CHARSET_SIZE];
    int fail[MAXN];
    int end[MAXN];
    int cnt;
    
    std::unordered_map<int, std::string> idToKeyword;
    int nextId;
    
public:
    KeywordExtractor() : cnt(0), nextId(1) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(end, 0, sizeof(end));
    }
    
    void addKeyword(const std::string& keyword) {
        if (keyword.empty()) return;
        
        idToKeyword[nextId] = keyword;
        insert(keyword, nextId);
        nextId++;
    }
    
private:
    void insert(const std::string& keyword, int keywordId) {
        int u = 0;
        for (char c : keyword) {
            int idx = static_cast<int>(c);
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        end[u] = keywordId;
    }
    
public:
    void build() {
        std::queue<int> q;
        for (int i = 0; i < CHARSET_SIZE; i++) {
            if (tree[0][i] != 0) {
                q.push(tree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (tree[u][i] != 0) {
                    fail[tree[u][i]] = tree[fail[u]][i];
                    q.push(tree[u][i]);
                } else {
                    tree[u][i] = tree[fail[u]][i];
                }
            }
        }
    }
    
    struct KeywordMatch {
        int keywordId;
        std::string keyword;
        int startPosition;
        int endPosition;
    };
    
    std::vector<KeywordMatch> extractKeywords(const std::string& text) {
        std::vector<KeywordMatch> matches;
        int u = 0;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text[i];
            u = tree[u][static_cast<int>(c)];
            
            int temp = u;
            while (temp != 0) {
                if (end[temp] != 0) {
                    KeywordMatch match;
                    match.keywordId = end[temp];
                    match.keyword = idToKeyword[end[temp]];
                    match.startPosition = i - match.keyword.length() + 1;
                    match.endPosition = i;
                    matches.push_back(match);
                }
                temp = fail[temp];
            }
        }
        
        return matches;
    }
};

// ==================== 应用4: 搜索引擎 - 多模式匹配 ====================

class SearchEngineIndexer {
private:
    static const int MAXN = 1000005;
    static const int CHARSET_SIZE = 128;
    
    int tree[MAXN][CHARSET_SIZE];
    int fail[MAXN];
    int end[MAXN];
    int cnt;
    
    std::unordered_map<int, std::unordered_set<int>> keywordToDocuments;
    int nextDocumentId;
    
public:
    SearchEngineIndexer() : cnt(0), nextDocumentId(1) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(end, 0, sizeof(end));
    }
    
    void indexDocument(const std::string& document, const std::unordered_set<std::string>& keywords) {
        int documentId = nextDocumentId++;
        
        for (const auto& keyword : keywords) {
            int keywordId = getKeywordId(keyword);
            keywordToDocuments[keywordId].insert(documentId);
            insert(keyword, keywordId);
        }
    }
    
private:
    int getKeywordId(const std::string& keyword) {
        return std::hash<std::string>{}(keyword);
    }
    
    void insert(const std::string& keyword, int keywordId) {
        int u = 0;
        for (char c : keyword) {
            int idx = static_cast<int>(c);
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        end[u] = keywordId;
    }
    
public:
    void build() {
        std::queue<int> q;
        for (int i = 0; i < CHARSET_SIZE; i++) {
            if (tree[0][i] != 0) {
                q.push(tree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (tree[u][i] != 0) {
                    fail[tree[u][i]] = tree[fail[u]][i];
                    q.push(tree[u][i]);
                } else {
                    tree[u][i] = tree[fail[u]][i];
                }
            }
        }
    }
    
    std::unordered_set<int> search(const std::string& query) {
        std::unordered_set<int> result;
        int u = 0;
        
        for (int i = 0; i < query.length(); i++) {
            char c = query[i];
            u = tree[u][static_cast<int>(c)];
            
            int temp = u;
            while (temp != 0) {
                if (end[temp] != 0) {
                    int keywordId = end[temp];
                    if (keywordToDocuments.find(keywordId) != keywordToDocuments.end()) {
                        const auto& documents = keywordToDocuments[keywordId];
                        result.insert(documents.begin(), documents.end());
                    }
                }
                temp = fail[temp];
            }
        }
        
        return result;
    }
};

// ==================== 主函数和测试用例 ====================

int main() {
    std::cout << "=== 测试恶意代码检测 ===" << std::endl;
    MalwareDetector detector;
    std::string code = "exec(\"malicious code\")";
    auto result = detector.detect(code);
    std::cout << "检测结果: " << (result.isMalicious ? "恶意代码" : "安全代码") << std::endl;
    
    std::cout << "\n=== 测试DNA序列匹配 ===" << std::endl;
    DNAMatcher matcher;
    matcher.insert("ATCG", 1);
    matcher.build();
    auto matches = matcher.findPatterns("ATCGATCG");
    std::cout << "找到 " << matches.size() << " 个匹配" << std::endl;
    
    std::cout << "\n=== 测试关键词提取 ===" << std::endl;
    KeywordExtractor extractor;
    extractor.addKeyword("测试");
    extractor.build();
    auto extractedKeywords = extractor.extractKeywords("This is a test text");
    std::cout << "Extracted " << extractedKeywords.size() << " keywords" << std::endl;
    
    std::cout << "\n=== Testing Search Engine Index ===" << std::endl;
    SearchEngineIndexer indexer;
    std::unordered_set<std::string> keywords = {"document", "test"};
    indexer.indexDocument("test document", keywords);
    indexer.build();
    auto docs = indexer.search("测试");
    std::cout << "搜索到 " << docs.size() << " 个文档" << std::endl;
    
    return 0;
}