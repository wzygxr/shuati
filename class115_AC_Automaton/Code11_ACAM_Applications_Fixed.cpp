#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <cstring>
#include <algorithm>
#include <unordered_map>
#include <unordered_set>

/**
 * AC自动机在实际应用中的扩展实现 - C++版本
 * 
 * 本文件实现了AC自动机在以下领域的应用：
 * 1. 网络安全：恶意代码检测
 * 2. 生物信息学：DNA序列匹配
 * 3. 自然语言处理：关键词提取
 * 4. 搜索引擎：多模式匹配
 */

// ==================== 应用1: 网络安全 - 恶意代码检测 ====================

class MalwareDetector {
private:
    static const int MAXN = 100005;
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
        
        for (size_t i = 0; i < code.length(); i++) {
            char c = code[i];
            u = tree[u][static_cast<int>(c)];
            
            if (danger[u]) {
                result.isMalicious = true;
                result.detectionPositions.push_back({static_cast<int>(i) - 10, static_cast<int>(i) + 10});
            }
        }
        
        return result;
    }
};

// ==================== 应用2: 生物信息学 - DNA序列匹配 ====================

class DNAMatcher {
private:
    static const int MAXN = 100005;
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
                throw std::invalid_argument("Invalid DNA character: " + std::string(1, c));
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
        
        for (size_t i = 0; i < dnaSequence.length(); i++) {
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
                    result.position = static_cast<int>(i);
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
    static const int MAXN = 100005;
    static const int CHARSET_SIZE = 256;
    
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
        if (keyword.empty()) {
            return;
        }
        
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
    
public:
    struct KeywordMatch {
        int keywordId;
        std::string keyword;
        int startPosition;
        int endPosition;
    };
    
    std::vector<KeywordMatch> extractKeywords(const std::string& text) {
        std::vector<KeywordMatch> matches;
        int u = 0;
        
        for (size_t i = 0; i < text.length(); i++) {
            char c = text[i];
            u = tree[u][static_cast<int>(c)];
            
            int temp = u;
            while (temp != 0) {
                if (end[temp] != 0) {
                    KeywordMatch match;
                    match.keywordId = end[temp];
                    match.keyword = idToKeyword[end[temp]];
                    match.startPosition = static_cast<int>(i) - static_cast<int>(idToKeyword[end[temp]].length()) + 1;
                    match.endPosition = static_cast<int>(i);
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
    static const int MAXN = 100005;
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
            int keywordId = std::hash<std::string>{}(keyword);
            if (keywordToDocuments.find(keywordId) == keywordToDocuments.end()) {
                keywordToDocuments[keywordId] = std::unordered_set<int>();
            }
            keywordToDocuments[keywordId].insert(documentId);
            
            insert(keyword, keywordId);
        }
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
    
public:
    std::unordered_set<int> search(const std::string& query) {
        std::unordered_set<int> result;
        int u = 0;
        
        for (char c : query) {
            u = tree[u][static_cast<int>(c)];
            
            int temp = u;
            while (temp != 0) {
                if (end[temp] != 0) {
                    int keywordId = end[temp];
                    if (keywordToDocuments.find(keywordId) != keywordToDocuments.end()) {
                        result.insert(keywordToDocuments[keywordId].begin(), keywordToDocuments[keywordId].end());
                    }
                }
                temp = fail[temp];
            }
        }
        
        return result;
    }
};

// ==================== 测试函数 ====================

int main() {
    std::cout << "=== Testing Malware Detector ===" << std::endl;
    MalwareDetector detector;
    
    std::string suspiciousCode = "public class Test { public static void main(String[] args) { Runtime.getRuntime().exec(\"cmd.exe\"); } }";
    auto result = detector.detect(suspiciousCode);
    
    std::cout << "Detection result: " << (result.isMalicious ? "Malicious code" : "Safe code") << std::endl;
    if (result.isMalicious) {
        std::cout << "Detection positions: " << result.detectionPositions.size() << " locations" << std::endl;
    }
    
    std::cout << "\n=== Testing DNA Matcher ===" << std::endl;
    DNAMatcher matcher;
    
    matcher.insert("ATCG", 1);
    matcher.insert("GCTA", 2);
    matcher.insert("TTAA", 3);
    matcher.build();
    
    std::string dnaSequence = "ATCGGCTATTAA";
    auto results = matcher.findPatterns(dnaSequence);
    
    std::cout << "Found " << results.size() << " matches in sequence '" << dnaSequence << "':" << std::endl;
    for (const auto& res : results) {
        std::cout << "Pattern " << res.patternId << " at position " << res.position << std::endl;
    }
    
    std::cout << "\n=== Testing Keyword Extractor ===" << std::endl;
    KeywordExtractor extractor;
    
    extractor.addKeyword("artificial intelligence");
    extractor.addKeyword("machine learning");
    extractor.addKeyword("deep learning");
    
    std::string text = "Artificial intelligence and machine learning are hot topics, and deep learning is an important branch of machine learning.";
    auto matches = extractor.extractKeywords(text);
    
    std::cout << "Extracted " << matches.size() << " keywords from text:" << std::endl;
    for (const auto& match : matches) {
        std::cout << "Keyword '" << match.keyword << "' at position [" << match.startPosition << "," << match.endPosition << "]" << std::endl;
    }
    
    std::cout << "\n=== Testing Search Engine Indexer ===" << std::endl;
    SearchEngineIndexer indexer;
    
    std::unordered_set<std::string> doc1Keywords = {"Java", "programming", "development"};
    indexer.indexDocument("Java Programming Guide", doc1Keywords);
    
    std::unordered_set<std::string> doc2Keywords = {"Python", "data science", "machine learning"};
    indexer.indexDocument("Python Data Science", doc2Keywords);
    
    indexer.build();
    
    auto docs = indexer.search("Java");
    std::cout << "Search 'Java' found " << docs.size() << " documents" << std::endl;
    
    std::vector<std::string> andQuery = {"Java", "programming"};
    std::unordered_set<int> andResults;
    // Simplified AND search implementation
    std::cout << "AND search implementation would go here" << std::endl;
    
    return 0;
}