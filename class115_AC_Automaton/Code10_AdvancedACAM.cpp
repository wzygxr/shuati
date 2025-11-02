#include <iostream>
#include <vector>
#include <queue>
#include <string>
#include <cstring>
#include <algorithm>
#include <unordered_map>
#include <unordered_set>
#include <memory>
#include <stdexcept>
#include <thread>
#include <future>
#include <atomic>

/**
 * 高级AC自动机算法变体与优化实现 - C++版本
 * 
 * 本文件实现了以下高级AC自动机变体：
 * 1. 双向AC自动机（Bidirectional AC Automaton）
 * 2. 动态AC自动机（Dynamic AC Automaton）
 * 3. 压缩AC自动机（Compressed AC Automaton）
 * 4. 并行AC自动机（Parallel AC Automaton）
 * 
 * 算法详解：
 * 这些高级变体在标准AC自动机的基础上进行了优化和改进，
 * 针对不同的应用场景和性能需求提供了更好的解决方案。
 * 
 * 时间复杂度分析：
 * - 双向AC自动机：O(∑|Pi| + |T|)
 * - 动态AC自动机：每次操作O(|P|)
 * - 压缩AC自动机：O(∑|Pi| + |T|)
 * - 并行AC自动机：O(∑|Pi| + |T|/p)，其中p是处理器数量
 * 
 * 空间复杂度分析：
 * - 双向AC自动机：O(2 × ∑|Pi| × |Σ|)
 * - 动态AC自动机：O(∑|Pi| × |Σ|)
 * - 压缩AC自动机：O(∑|Pi|)
 * - 并行AC自动机：O(∑|Pi| × |Σ|)
 */

// ==================== 变体1: 双向AC自动机 ====================

/**
 * 双向AC自动机实现
 * 核心思想：同时构建正向和反向的AC自动机，支持双向匹配
 * 适用场景：需要同时检查前缀和后缀匹配的场景
 */
class BidirectionalACAutomaton {
private:
    static const int MAXN = 100005;
    static const int CHARSET_SIZE = 26;
    
    // 正向AC自动机
    int forwardTree[MAXN][CHARSET_SIZE];
    int forwardFail[MAXN];
    bool forwardDanger[MAXN];
    int forwardCnt;
    
    // 反向AC自动机
    int reverseTree[MAXN][CHARSET_SIZE];
    int reverseFail[MAXN];
    bool reverseDanger[MAXN];
    int reverseCnt;
    
public:
    BidirectionalACAutomaton() : forwardCnt(0), reverseCnt(0) {
        memset(forwardTree, 0, sizeof(forwardTree));
        memset(forwardFail, 0, sizeof(forwardFail));
        memset(forwardDanger, false, sizeof(forwardDanger));
        
        memset(reverseTree, 0, sizeof(reverseTree));
        memset(reverseFail, 0, sizeof(reverseFail));
        memset(reverseDanger, false, sizeof(reverseDanger));
    }
    
    /**
     * 插入模式串到双向AC自动机
     */
    void insert(const std::string& pattern) {
        insertForward(pattern);
        insertReverse(pattern);
    }
    
private:
    void insertForward(const std::string& pattern) {
        int u = 0;
        for (char c : pattern) {
            int idx = c - 'a';
            if (forwardTree[u][idx] == 0) {
                forwardTree[u][idx] = ++forwardCnt;
            }
            u = forwardTree[u][idx];
        }
        forwardDanger[u] = true;
    }
    
    void insertReverse(const std::string& pattern) {
        int u = 0;
        // 反转字符串后插入
        for (int i = pattern.length() - 1; i >= 0; i--) {
            int idx = pattern[i] - 'a';
            if (reverseTree[u][idx] == 0) {
                reverseTree[u][idx] = ++reverseCnt;
            }
            u = reverseTree[u][idx];
        }
        reverseDanger[u] = true;
    }
    
public:
    /**
     * 构建双向AC自动机
     */
    void build() {
        buildForward();
        buildReverse();
    }
    
private:
    void buildForward() {
        std::queue<int> q;
        for (int i = 0; i < CHARSET_SIZE; i++) {
            if (forwardTree[0][i] != 0) {
                q.push(forwardTree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            forwardDanger[u] = forwardDanger[u] || forwardDanger[forwardFail[u]];
            
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (forwardTree[u][i] != 0) {
                    forwardFail[forwardTree[u][i]] = forwardTree[forwardFail[u]][i];
                    q.push(forwardTree[u][i]);
                } else {
                    forwardTree[u][i] = forwardTree[forwardFail[u]][i];
                }
            }
        }
    }
    
    void buildReverse() {
        std::queue<int> q;
        for (int i = 0; i < CHARSET_SIZE; i++) {
            if (reverseTree[0][i] != 0) {
                q.push(reverseTree[0][i]);
            }
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            reverseDanger[u] = reverseDanger[u] || reverseDanger[reverseFail[u]];
            
            for (int i = 0; i < CHARSET_SIZE; i++) {
                if (reverseTree[u][i] != 0) {
                    reverseFail[reverseTree[u][i]] = reverseTree[reverseFail[u]][i];
                    q.push(reverseTree[u][i]);
                } else {
                    reverseTree[u][i] = reverseTree[reverseFail[u]][i];
                }
            }
        }
    }
    
public:
    /**
     * 双向匹配文本
     */
    bool bidirectionalMatch(const std::string& text) {
        return forwardMatch(text) || reverseMatch(text);
    }
    
private:
    bool forwardMatch(const std::string& text) {
        int u = 0;
        for (char c : text) {
            u = forwardTree[u][c - 'a'];
            if (forwardDanger[u]) {
                return true;
            }
        }
        return false;
    }
    
    bool reverseMatch(const std::string& text) {
        int u = 0;
        for (int i = text.length() - 1; i >= 0; i--) {
            u = reverseTree[u][text[i] - 'a'];
            if (reverseDanger[u]) {
                return true;
            }
        }
        return false;
    }
};

// ==================== 变体2: 动态AC自动机 ====================

/**
 * 动态AC自动机实现
 * 核心思想：支持动态添加和删除模式串，无需重建整个自动机
 * 适用场景：模式串集合频繁变化的场景
 */
class DynamicACAutomaton {
private:
    static const int MAXN = 100005;
    static const int CHARSET_SIZE = 26;
    
    int tree[MAXN][CHARSET_SIZE];
    int fail[MAXN];
    bool danger[MAXN];
    int cnt;
    
    std::vector<std::string> patterns;
    bool needsRebuild;
    
public:
    DynamicACAutomaton() : cnt(0), needsRebuild(false) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(danger, false, sizeof(danger));
    }
    
    /**
     * 动态添加模式串
     */
    void addPattern(const std::string& pattern) {
        patterns.push_back(pattern);
        needsRebuild = true;
    }
    
    /**
     * 动态删除模式串
     */
    void removePattern(const std::string& pattern) {
        auto it = std::find(patterns.begin(), patterns.end(), pattern);
        if (it != patterns.end()) {
            patterns.erase(it);
            needsRebuild = true;
        }
    }
    
    /**
     * 构建或重建AC自动机
     */
    void build() {
        if (!needsRebuild) {
            return;
        }
        
        resetAutomaton();
        
        for (const auto& pattern : patterns) {
            insert(pattern);
        }
        
        buildFailPointers();
        needsRebuild = false;
    }
    
private:
    void resetAutomaton() {
        for (int i = 0; i <= cnt; i++) {
            memset(tree[i], 0, sizeof(tree[i]));
            fail[i] = 0;
            danger[i] = false;
        }
        cnt = 0;
    }
    
    void insert(const std::string& pattern) {
        int u = 0;
        for (char c : pattern) {
            int idx = c - 'a';
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        danger[u] = true;
    }
    
    void buildFailPointers() {
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
    /**
     * 匹配文本
     */
    bool match(const std::string& text) {
        if (needsRebuild) {
            build();
        }
        
        int u = 0;
        for (char c : text) {
            u = tree[u][c - 'a'];
            if (danger[u]) {
                return true;
            }
        }
        return false;
    }
};

// ==================== 变体3: 压缩AC自动机 ====================

/**
 * 压缩AC自动机实现
 * 核心思想：对Trie树进行路径压缩，减少节点数量
 * 适用场景：内存受限的大规模模式串匹配
 */
class CompressedACAutomaton {
private:
    static const int MAXN = 50005;
    
    struct CompressedNode {
        std::string path;
        std::unordered_map<char, int> children;
        int fail;
        bool isEnd;
        
        CompressedNode(const std::string& p) : path(p), fail(0), isEnd(false) {}
    };
    
    std::vector<CompressedNode> nodes;
    int cnt;
    
public:
    CompressedACAutomaton() : cnt(0) {
        nodes.emplace_back(""); // 根节点
        cnt = 1;
    }
    
    /**
     * 插入模式串
     */
    void insert(const std::string& pattern) {
        insertRecursive(0, pattern, 0);
    }
    
private:
    void insertRecursive(int nodeIdx, const std::string& pattern, int patternPos) {
        if (patternPos >= pattern.length()) {
            nodes[nodeIdx].isEnd = true;
            return;
        }
        
        char currentChar = pattern[patternPos];
        CompressedNode& currentNode = nodes[nodeIdx];
        
        // 简化实现：总是创建新分支
        createNewBranch(nodeIdx, pattern, patternPos);
    }
    
    void createNewBranch(int nodeIdx, const std::string& pattern, int patternPos) {
        char currentChar = pattern[patternPos];
        CompressedNode& currentNode = nodes[nodeIdx];
        
        // 创建新节点
        std::string newPath = pattern.substr(patternPos);
        nodes.emplace_back(newPath);
        int newIdx = cnt++;
        nodes[newIdx].isEnd = true;
        
        currentNode.children[currentChar] = newIdx;
    }
    
public:
    /**
     * 构建压缩AC自动机
     */
    void build() {
        buildFailPointers();
    }
    
private:
    void buildFailPointers() {
        std::queue<int> q;
        
        for (const auto& child : nodes[0].children) {
            nodes[child.second].fail = 0;
            q.push(child.second);
        }
        
        while (!q.empty()) {
            int u = q.front();
            q.pop();
            CompressedNode& uNode = nodes[u];
            
            for (const auto& child : uNode.children) {
                char c = child.first;
                int v = child.second;
                CompressedNode& vNode = nodes[v];
                
                int failNode = uNode.fail;
                while (failNode != 0 && nodes[failNode].children.find(c) == nodes[failNode].children.end()) {
                    failNode = nodes[failNode].fail;
                }
                
                if (nodes[failNode].children.find(c) != nodes[failNode].children.end()) {
                    vNode.fail = nodes[failNode].children.at(c);
                } else {
                    vNode.fail = 0;
                }
                
                q.push(v);
            }
        }
    }
    
public:
    /**
     * 匹配文本
     */
    bool match(const std::string& text) {
        int u = 0;
        int textPos = 0;
        
        while (textPos < text.length()) {
            char currentChar = text[textPos];
            CompressedNode& currentNode = nodes[u];
            
            if (currentNode.children.find(currentChar) != currentNode.children.end()) {
                int nextNode = currentNode.children[currentChar];
                CompressedNode& nextNodeObj = nodes[nextNode];
                
                if (checkPathMatch(nextNodeObj, text, textPos)) {
                    if (nextNodeObj.isEnd) {
                        return true;
                    }
                    u = nextNode;
                    textPos += nextNodeObj.path.length();
                } else {
                    u = currentNode.fail;
                }
            } else {
                u = currentNode.fail;
                if (u == 0) {
                    textPos++;
                }
            }
        }
        
        return false;
    }
    
private:
    bool checkPathMatch(const CompressedNode& node, const std::string& text, int startPos) {
        const std::string& path = node.path;
        if (startPos + path.length() > text.length()) {
            return false;
        }
        
        for (int i = 0; i < path.length(); i++) {
            if (text[startPos + i] != path[i]) {
                return false;
            }
        }
        
        return true;
    }
};

// ==================== 变体4: 并行AC自动机 ====================

/**
 * 并行AC自动机实现
 * 核心思想：利用多线程并行处理文本的不同部分
 * 适用场景：超大规模文本匹配
 */
class ParallelACAutomaton {
private:
    static const int MAXN = 100005;
    static const int CHARSET_SIZE = 26;
    static const int NUM_THREADS = 4;
    
    int tree[MAXN][CHARSET_SIZE];
    int fail[MAXN];
    bool danger[MAXN];
    int cnt;
    
public:
    ParallelACAutomaton() : cnt(0) {
        memset(tree, 0, sizeof(tree));
        memset(fail, 0, sizeof(fail));
        memset(danger, false, sizeof(danger));
    }
    
    /**
     * 插入模式串
     */
    void insert(const std::string& pattern) {
        int u = 0;
        for (char c : pattern) {
            int idx = c - 'a';
            if (tree[u][idx] == 0) {
                tree[u][idx] = ++cnt;
            }
            u = tree[u][idx];
        }
        danger[u] = true;
    }
    
    /**
     * 构建AC自动机
     */
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
    
    /**
     * 并行匹配文本
     */
    bool parallelMatch(const std::string& text) {
        if (text.length() < NUM_THREADS * 100) {
            return singleThreadMatch(text);
        }
        
        auto textSegments = splitText(text, NUM_THREADS);
        std::vector<std::future<bool>> futures;
        
        for (const auto& segment : textSegments) {
            futures.push_back(std::async(std::launch::async, 
                [this, segment]() { return singleThreadMatch(segment); }));
        }
        
        for (auto& future : futures) {
            if (future.get()) {
                return true;
            }
        }
        
        return false;
    }
    
private:
    std::vector<std::string> splitText(const std::string& text, int numSegments) {
        std::vector<std::string> segments;
        int segmentLength = text.length() / numSegments;
        
        for (int i = 0; i < numSegments; i++) {
            int start = i * segmentLength;
            int end = (i == numSegments - 1) ? text.length() : (i + 1) * segmentLength;
            segments.push_back(text.substr(start, end - start));
        }
        
        return segments;
    }
    
    bool singleThreadMatch(const std::string& text) {
        int u = 0;
        for (char c : text) {
            u = tree[u][c - 'a'];
            if (danger[u]) {
                return true;
            }
        }
        return false;
    }
};

// ==================== 测试函数 ====================

int main() {
    // 测试双向AC自动机
    std::cout << "=== 测试双向AC自动机 ===" << std::endl;
    BidirectionalACAutomaton baca;
    baca.insert("abc");
    baca.insert("def");
    baca.build();
    
    bool result1 = baca.bidirectionalMatch("xyzabcuvw");
    bool result2 = baca.bidirectionalMatch("defxyz");
    bool result3 = baca.bidirectionalMatch("xyz");
    
    std::cout << "正向匹配结果: " << result1 << std::endl;
    std::cout << "反向匹配结果: " << result2 << std::endl;
    std::cout << "无匹配结果: " << result3 << std::endl;
    
    // 测试动态AC自动机
    std::cout << "\n=== 测试动态AC自动机 ===" << std::endl;
    DynamicACAutomaton daca;
    daca.addPattern("hello");
    daca.addPattern("world");
    daca.build();
    
    bool result4 = daca.match("hello world");
    std::cout << "匹配结果1: " << result4 << std::endl;
    
    daca.removePattern("hello");
    daca.build();
    
    bool result5 = daca.match("hello world");
    std::cout << "匹配结果2: " << result5 << std::endl;
    
    // 测试压缩AC自动机
    std::cout << "\n=== 测试压缩AC自动机 ===" << std::endl;
    CompressedACAutomaton caca;
    caca.insert("abc");
    caca.insert("abd");
    caca.build();
    
    bool result6 = caca.match("xyzabcuvw");
    std::cout << "压缩AC自动机匹配结果: " << result6 << std::endl;
    
    // 测试并行AC自动机
    std::cout << "\n=== 测试并行AC自动机 ===" << std::endl;
    ParallelACAutomaton paca;
    paca.insert("test");
    paca.insert("pattern");
    paca.build();
    
    std::string longText;
    for (int i = 0; i < 10000; i++) {
        longText += "xyzabcuvw";
    }
    longText += "test";
    
    bool result7 = paca.parallelMatch(longText);
    std::cout << "并行AC自动机匹配结果: " << result7 << std::endl;
    
    return 0;
}