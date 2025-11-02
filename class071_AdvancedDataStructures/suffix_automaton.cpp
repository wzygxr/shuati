/**
 * 后缀自动机（Suffix Automaton）实现
 * 后缀自动机是一个能够表示一个字符串的所有子串的最小DFA
 * 主要特性：
 * 1. 状态数和转移边数都是O(n)级别
 * 2. 每个状态代表一个endpos等价类
 * 3. 支持高效查询子串、不同子串计数等操作
 * 时间复杂度：构建O(n)，查询O(m)
 * 空间复杂度：O(n)
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>
#include <stdexcept>
using namespace std;

class SuffixAutomaton {
private:
    /**
     * 后缀自动机的状态节点
     */
    struct State {
        unordered_map<char, int> next; // 转移函数
        int length;                   // 该状态能接受的最长子串长度
        int link;                     // 后缀链接（suffix link）
        int endposSize;               // endpos集合的大小
        bool isClone;                 // 是否是克隆节点

        State(int len) : length(len), link(-1), endposSize(0), isClone(false) {}
    };

    vector<State> states;   // 所有状态
    int last;               // 上一个状态的索引
    int size;               // 当前状态数量
    string text;            // 原始文本

    /**
     * 扩展后缀自动机，添加一个字符
     * @param c 要添加的字符
     */
    void extend(char c) {
        // 创建新状态cur
        int cur = size++;
        states.emplace_back(states[last].length + 1);
        states[cur].endposSize = 1; // 新状态的endpos大小为1

        // 从last开始，沿着后缀链接回溯，添加转移
        int p = last;
        while (p >= 0 && states[p].next.find(c) == states[p].next.end()) {
            states[p].next[c] = cur;
            p = states[p].link;
        }

        if (p == -1) {
            // 如果没有找到含有c转移的状态，将cur的后缀链接指向初始状态
            states[cur].link = 0;
        } else {
            int q = states[p].next[c];
            if (states[p].length + 1 == states[q].length) {
                // 如果q已经是p通过c转移后的正确状态
                states[cur].link = q;
            } else {
                // 需要分裂状态q
                int clone = size++;
                states.emplace_back(states[p].length + 1);
                states[clone].next = states[q].next;  // 复制转移
                states[clone].link = states[q].link;   // 复制后缀链接
                states[clone].isClone = true;          // 标记为克隆节点

                // 更新q和cur的后缀链接
                states[q].link = clone;
                states[cur].link = clone;

                // 从p开始，沿着后缀链接回溯，更新转移
                while (p >= 0 && states[p].next.find(c) != states[p].next.end() && 
                       states[p].next[c] == q) {
                    states[p].next[c] = clone;
                    p = states[p].link;
                }
            }
        }

        // 更新last为新状态
        last = cur;
    }

    /**
     * 计算每个状态的endpos集合大小
     * 基于后缀链接树进行后序遍历累加
     */
    void calculateEndposSize() {
        // 根据length对状态进行排序（用于后序遍历后缀链接树）
        vector<int> order(size);
        for (int i = 0; i < size; i++) {
            order[i] = i;
        }
        sort(order.begin(), order.end(), [this](int a, int b) {
            return states[a].length > states[b].length;
        });

        // 后序遍历，累加endpos大小
        for (int v : order) {
            if (states[v].link != -1 && !states[v].isClone) {
                states[states[v].link].endposSize += states[v].endposSize;
            }
        }
    }

    /**
     * 递归查找指定长度的子串
     */
    string findSubstringByLength(int state, int targetLength, string current) const {
        if (states[state].length == targetLength) {
            return current;
        }

        for (const auto& entry : states[state].next) {
            char c = entry.first;
            int nextState = entry.second;
            if (states[nextState].length <= targetLength) {
                string result = findSubstringByLength(nextState, targetLength, current + c);
                if (!result.empty()) {
                    return result;
                }
            }
        }

        return "";
    }

public:
    /**
     * 构造函数，构建后缀自动机
     * @param text 输入文本
     */
    SuffixAutomaton(const string& t) : text(t) {
        states.reserve(2 * text.length()); // 预分配空间，状态数最多为2n-1
        states.emplace_back(0); // 初始状态
        last = 0;
        size = 1;

        // 逐个字符构建自动机
        for (char c : text) {
            extend(c);
        }

        // 计算endpos集合大小
        calculateEndposSize();
    }

    /**
     * 检查字符串s是否是原始文本的子串
     * @param s 要检查的字符串
     * @return 如果是子串返回true，否则返回false
     */
    bool contains(const string& s) const {
        int state = 0; // 从初始状态开始
        for (char c : s) {
            auto it = states[state].next.find(c);
            if (it == states[state].next.end()) {
                return false; // 没有对应的转移，不是子串
            }
            state = it->second;
        }
        return true; // 成功匹配所有字符
    }

    /**
     * 计算不同子串的数量
     * 利用性质：不同子串数量 = Σ (length[state] - length[link[state]])
     * @return 不同子串的数量
     */
    long long countDistinctSubstrings() const {
        long long count = 0;
        for (int i = 1; i < size; i++) { // 跳过初始状态
            count += states[i].length - states[states[i].link].length;
        }
        return count;
    }

    /**
     * 计算子串s在原文本中出现的次数
     * @param s 要查询的子串
     * @return 出现次数
     */
    int countOccurrences(const string& s) const {
        // 找到对应s的状态
        int state = 0;
        for (char c : s) {
            auto it = states[state].next.find(c);
            if (it == states[state].next.end()) {
                return 0; // 不是子串，出现次数为0
            }
            state = it->second;
        }

        return states[state].endposSize;
    }

    /**
     * 找出所有出现次数至少为k次的子串中，最长的那个
     * @param k 最小出现次数
     * @return 最长的满足条件的子串
     */
    string findLongestSubstringWithKOccurrences(int k) const {
        if (k <= 0) {
            throw invalid_argument("k必须为正整数");
        }

        int maxLength = 0;

        // 遍历所有状态，找到endposSize >= k的状态，且length最大
        for (int i = 1; i < size; i++) {
            if (states[i].endposSize >= k && states[i].length > maxLength) {
                maxLength = states[i].length;
            }
        }

        if (maxLength == 0) {
            return "";
        }

        // 找到对应的子串
        return findSubstringByLength(0, maxLength, "");
    }

    /**
     * 找出文本的最长重复子串
     * @return 最长重复子串
     */
    string findLongestRepeatedSubstring() const {
        return findLongestSubstringWithKOccurrences(2);
    }

    /**
     * 获取后缀自动机的状态数量
     * @return 状态数量
     */
    int getStateCount() const {
        return size;
    }

    /**
     * 获取后缀自动机的信息
     * @return 状态和转移信息的字符串表示
     */
    string toString() const {
        string result;
        result += "后缀自动机状态信息：\n";
        result += "文本: " + text + "\n";
        result += "状态数量: " + to_string(size) + "\n";

        for (int i = 0; i < size; i++) {
            const State& state = states[i];
            string stateInfo = "状态 " + to_string(i) + ": length=" + to_string(state.length) + ", link=" + 
                              to_string(state.link) + ", endposSize=" + to_string(state.endposSize);
            if (state.isClone) {
                stateInfo += " (克隆)";
            }
            result += stateInfo + "\n";

            result += "  转移: ";
            for (const auto& entry : state.next) {
                result += "[" + string(1, entry.first) + " -> " + to_string(entry.second) + "] ";
            }
            result += "\n";
        }

        return result;
    }
};

int main() {
    // 测试用例1：基本功能测试
    string text1 = "banana";
    SuffixAutomaton sam1(text1);
    cout << "=== 测试用例1 ===" << endl;
    cout << "文本: " << text1 << endl;
    cout << "状态数量: " << sam1.getStateCount() << endl;
    cout << "不同子串数量: " << sam1.countDistinctSubstrings() << endl; // 应该是15

    // 测试子串检查
    cout << "包含'an': " << (sam1.contains("an") ? "true" : "false") << endl; // true
    cout << "包含'na': " << (sam1.contains("na") ? "true" : "false") << endl; // true
    cout << "包含'xyz': " << (sam1.contains("xyz") ? "true" : "false") << endl; // false

    // 测试出现次数
    cout << "'a'出现次数: " << sam1.countOccurrences("a") << endl; // 3
    cout << "'na'出现次数: " << sam1.countOccurrences("na") << endl; // 2

    // 测试最长重复子串
    cout << "最长重复子串: " << sam1.findLongestRepeatedSubstring() << endl; // "ana"

    // 测试用例2：边界情况
    string text2 = "aaa";
    SuffixAutomaton sam2(text2);
    cout << "\n=== 测试用例2 ===" << endl;
    cout << "文本: " << text2 << endl;
    cout << "不同子串数量: " << sam2.countDistinctSubstrings() << endl; // 3
    cout << "最长重复子串: " << sam2.findLongestRepeatedSubstring() << endl; // "aa"

    // 测试用例3：更长文本
    string text3 = "mississippi";
    SuffixAutomaton sam3(text3);
    cout << "\n=== 测试用例3 ===" << endl;
    cout << "文本: " << text3 << endl;
    cout << "状态数量: " << sam3.getStateCount() << endl;
    cout << "包含'issi': " << (sam3.contains("issi") ? "true" : "false") << endl; // true
    cout << "'issi'出现次数: " << sam3.countOccurrences("issi") << endl; // 2

    return 0;
}