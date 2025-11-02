/**
 * 后缀数组（Suffix Array）实现
 * 包含：
 * 1. 倍增法构造后缀数组 O(n log n)
 * 2. 计算height数组 O(n)
 * 3. ST表预处理用于LCP查询 O(n log n) 预处理，O(1) 查询
 * 时间复杂度：构造O(n log n)，查询O(1)
 * 空间复杂度：O(n log n)
 */

#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>
#include <stdexcept>
using namespace std;

class SuffixArray {
private:
    string text;          // 原始文本
    vector<int> suffixArray;    // 后缀数组，存储排序后的后缀起始位置
    vector<int> rank;           // rank[i]表示起始位置为i的后缀的排名
    vector<int> height;         // height[i]表示后缀数组中第i个和第i-1个后缀的LCP
    vector<vector<int>> stTable; // ST表，用于LCP区间查询
    int logN;                   // log2(n)的上界
    int n;                      // 文本长度

    /**
     * 获取位置i开始的后缀，向后移动k位后的排名
     */
    int getRank(int i, int k) const {
        return (i + k < n) ? rank[i + k] : -1;
    }

    /**
     * 计算区间[l, r]的最小值
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     * @return 区间最小值
     */
    int queryMin(int l, int r) const {
        if (l > r) {
            swap(l, r);
        }

        // 计算区间长度的对数
        int len = r - l + 1;
        int k = 0;
        while ((1 << (k + 1)) <= len) {
            k++;
        }

        // 查询最小值
        return min(
            stTable[k][l],
            stTable[k][r - (1 << k) + 1]
        );
    }

    /**
     * 比较后缀函数，用于排序
     */
    struct SuffixComparator {
        const string& text;
        const vector<int>& rank;
        int k;

        SuffixComparator(const string& t, const vector<int>& r, int k_val) 
            : text(t), rank(r), k(k_val) {}

        bool operator()(int a, int b) const {
            if (rank[a] != rank[b]) {
                return rank[a] < rank[b];
            }
            int rankA = (a + k < text.size()) ? rank[a + k] : -1;
            int rankB = (b + k < text.size()) ? rank[b + k] : -1;
            return rankA < rankB;
        }
    };

    /**
     * 比较以pos开始的后缀和模式串
     */
    int compareSuffixWithPattern(int pos, const string& pattern) const {
        int m = pattern.length();
        for (int i = 0; i < m; i++) {
            if (pos + i >= n) {
                // 后缀已结束，模式串未结束，后缀小
                return -1;
            }
            char c1 = text[pos + i];
            char c2 = pattern[i];
            if (c1 != c2) {
                return c1 < c2 ? -1 : 1;
            }
        }
        // 前缀相同，说明匹配
        return 0;
    }

public:
    /**
     * 构造函数，构建后缀数组
     * @param text 输入文本
     */
    SuffixArray(const string& t) : text(t) {
        n = text.length();

        // 构造后缀数组
        buildSuffixArray();

        // 计算height数组
        computeHeight();

        // 构建ST表
        buildST();
    }

    /**
     * 使用倍增法构建后缀数组
     */
    void buildSuffixArray() {
        // 初始化
        suffixArray.resize(n);
        rank.resize(n);

        // 初始阶段：每个字符单独排名
        for (int i = 0; i < n; i++) {
            suffixArray[i] = i;
            rank[i] = static_cast<unsigned char>(text[i]); // 初始排名为字符的ASCII值
        }

        // 倍增排序
        for (int k = 1; k < n; k *= 2) {
            // 使用自定义的比较器进行排序
            SuffixComparator comp(text, rank, k);
            sort(suffixArray.begin(), suffixArray.end(), comp);

            // 更新排名
            vector<int> newRank(n);
            newRank[suffixArray[0]] = 0;
            for (int i = 1; i < n; i++) {
                // 如果当前后缀与前一个后缀的排名相同，则给予相同的排名
                int prev = suffixArray[i-1];
                int curr = suffixArray[i];
                if (rank[prev] == rank[curr] && getRank(prev, k) == getRank(curr, k)) {
                    newRank[curr] = newRank[prev];
                } else {
                    newRank[curr] = newRank[prev] + 1;
                }
            }

            // 将临时排名复制回rank数组
            rank = newRank;
        }
    }

    /**
     * 计算height数组
     * 利用性质：height[rank[i]] >= height[rank[i-1]] - 1
     */
    void computeHeight() {
        height.resize(n, 0);
        vector<int> rankToSuffix(n); // rankToSuffix[r]表示排名为r的后缀的起始位置

        // 构建rank到suffix的映射
        for (int i = 0; i < n; i++) {
            rankToSuffix[rank[i]] = i;
        }

        int k = 0; // 当前LCP长度
        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r == 0) {
                height[r] = 0; // 排名为0的后缀没有前一个后缀
                continue;
            }

            // 获取前一个排名的后缀起始位置
            int j = rankToSuffix[r - 1];

            // 从上一轮的k-1开始比较（利用性质优化）
            if (k > 0) k--;

            // 扩展LCP
            while (i + k < n && j + k < n && text[i + k] == text[j + k]) {
                k++;
            }

            height[r] = k;
        }
    }

    /**
     * 构建ST表用于RMQ（区间最小值查询）
     */
    void buildST() {
        // 计算log2(n)的上界
        logN = 0;
        int temp = 1;
        while (temp * 2 <= n) {
            temp *= 2;
            logN++;
        }

        // 初始化ST表
        stTable.resize(logN + 1, vector<int>(n));

        // 填充第0层（原始height数组）
        for (int i = 0; i < n; i++) {
            stTable[0][i] = height[i];
        }

        // 构建其余层
        for (int k = 1; k <= logN; k++) {
            for (int i = 0; i + (1 << k) <= n; i++) {
                // stTable[k][i] = min(stTable[k-1][i], stTable[k-1][i + (1 << (k-1))])
                stTable[k][i] = min(
                    stTable[k-1][i],
                    stTable[k-1][i + (1 << (k-1))]
                );
            }
        }
    }

    /**
     * 计算两个后缀的最长公共前缀（LCP）
     * @param i 第一个后缀的起始位置
     * @param j 第二个后缀的起始位置
     * @return LCP长度
     */
    int getLCP(int i, int j) const {
        if (i < 0 || i >= n || j < 0 || j >= n) {
            throw out_of_range("后缀起始位置超出范围");
        }

        if (i == j) {
            return n - i; // 同一个后缀，LCP就是后缀长度
        }

        // 获取两个后缀的排名
        int r1 = rank[i];
        int r2 = rank[j];

        // 确保r1 < r2
        if (r1 > r2) {
            swap(r1, r2);
        }

        // 后缀排序中，LCP(r1, r2) = min{height[r1+1], height[r1+2], ..., height[r2]}
        return queryMin(r1 + 1, r2);
    }

    /**
     * 获取后缀数组
     * @return 后缀数组
     */
    vector<int> getSuffixArray() const {
        return suffixArray;
    }

    /**
     * 获取height数组
     * @return height数组
     */
    vector<int> getHeightArray() const {
        return height;
    }

    /**
     * 获取rank数组
     * @return rank数组
     */
    vector<int> getRankArray() const {
        return rank;
    }

    /**
     * 查找模式串在文本串中所有出现的位置
     * @param pattern 模式串
     * @return 所有匹配位置的起始索引数组
     */
    vector<int> findPattern(const string& pattern) const {
        vector<int> matches;

        int m = pattern.length();
        if (m == 0) {
            // 空模式串匹配所有位置
            for (int i = 0; i <= n; i++) {
                matches.push_back(i);
            }
            return matches;
        }

        if (m > n) {
            return matches; // 无匹配
        }

        // 使用二分查找找到第一个匹配的位置
        int left = 0, right = n - 1;
        int firstPos = -1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = compareSuffixWithPattern(suffixArray[mid], pattern);

            if (cmp >= 0) {
                if (cmp == 0) {
                    firstPos = mid;
                }
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        if (firstPos == -1) {
            return matches; // 无匹配
        }

        // 找到最后一个匹配的位置
        left = firstPos;
        right = n - 1;
        int lastPos = firstPos;

        while (left <= right) {
            int mid = (left + right) / 2;
            if (compareSuffixWithPattern(suffixArray[mid], pattern) == 0) {
                lastPos = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        // 收集所有匹配位置
        for (int i = firstPos; i <= lastPos; i++) {
            matches.push_back(suffixArray[i]);
        }

        // 按位置排序
        sort(matches.begin(), matches.end());

        return matches;
    }

    /**
     * 计算文本中的不同子串数量
     * 利用后缀数组性质：不同子串数量 = n(n+1)/2 - Σheight[i]
     * @return 不同子串数量
     */
    long long countDistinctSubstrings() const {
        long long total = static_cast<long long>(n) * (n + 1) / 2;
        long long sumHeight = 0;
        for (int h : height) {
            sumHeight += h;
        }
        return total - sumHeight;
    }
};

int main() {
    // 测试用例1：基本功能测试
    string text1 = "banana";
    SuffixArray sa1(text1);
    cout << "=== 测试用例1 ===" << endl;
    cout << "文本: " << text1 << endl;
    
    cout << "后缀数组: ";
    for (int pos : sa1.getSuffixArray()) {
        cout << pos << " ";
    }
    cout << endl;
    
    cout << "排名数组: ";
    for (int r : sa1.getRankArray()) {
        cout << r << " ";
    }
    cout << endl;
    
    cout << "Height数组: ";
    for (int h : sa1.getHeightArray()) {
        cout << h << " ";
    }
    cout << endl;
    
    // 测试LCP查询
    cout << "LCP(1, 3) (ana和ana): " << sa1.getLCP(1, 3) << endl; // 应该是3
    cout << "LCP(0, 2) (banana和nana): " << sa1.getLCP(0, 2) << endl; // 应该是0
    
    // 测试模式匹配
    vector<int> matches1 = sa1.findPattern("ana");
    cout << "查找模式'ana': ";
    for (int pos : matches1) {
        cout << pos << " ";
    }
    cout << endl; // 应该是1 3
    
    // 测试不同子串数量
    cout << "不同子串数量: " << sa1.countDistinctSubstrings() << endl; // 应该是15
    
    // 测试用例2：边界情况
    string text2 = "aaa";
    SuffixArray sa2(text2);
    cout << "\n=== 测试用例2 ===" << endl;
    cout << "文本: " << text2 << endl;
    cout << "不同子串数量: " << sa2.countDistinctSubstrings() << endl; // 应该是3
    
    // 测试用例3：更长文本
    string text3 = "mississippi";
    SuffixArray sa3(text3);
    cout << "\n=== 测试用例3 ===" << endl;
    cout << "文本: " << text3 << endl;
    cout << "LCP(1, 4) (issi和ippi): " << sa3.getLCP(1, 4) << endl;
    vector<int> matches3 = sa3.findPattern("issi");
    cout << "查找模式'issi': ";
    for (int pos : matches3) {
        cout << pos << " ";
    }
    cout << endl;
    
    return 0;
}