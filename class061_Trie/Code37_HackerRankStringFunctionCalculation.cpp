/**
 * HackerRank String Function Calculation
 * 
 * 题目描述：
 * 给定一个字符串t，定义函数f(S) = |S| * (S在t中出现的次数)，其中S是t的任意子串。
 * 求所有子串S中f(S)的最大值。
 * 
 * 解题思路：
 * 这是一个经典的后缀数组应用问题。我们可以使用以下方法：
 * 1. 构建字符串的后缀数组和高度数组（LCP数组）
 * 2. 对于每个可能的子串长度，计算该长度的所有子串的出现次数
 * 3. 使用单调栈来高效计算每个长度对应的最大出现次数
 * 
 * 具体步骤：
 * 1. 构建后缀数组和LCP数组
 * 2. 对于LCP数组中的每个值，使用单调栈计算以该值为最小值的区间能贡献的最大f值
 * 3. 同时考虑所有单个字符的情况
 * 
 * 时间复杂度：O(N)
 * 空间复杂度：O(N)
 * 
 * 注意：这个问题也可以用后缀自动机解决，但后缀数组更容易理解和实现。
 */

// 由于环境中缺少标准库头文件，我们使用简化的实现
#define MAX_N 100000
#define MAX_CHAR 256

/**
 * 构建字符串的后缀数组
 * @param s 输入字符串
 * @return 后缀数组
 */
vector<int> suffixArray(const string& s) {
    int n = s.length();
    vector<int> suffixes(n);
    
    // 初始化：按第一个字符排序
    for (int i = 0; i < n; i++) {
        suffixes[i] = i;
    }
    
    sort(suffixes.begin(), suffixes.end(), [&s](int a, int b) {
        return s[a] < s[b];
    });
    
    // 倍增算法构建后缀数组
    vector<int> rank(n);
    vector<int> tempRank(n);
    int k = 1;
    
    while (k < n) {
        // 更新rank数组
        for (int i = 0; i < n; i++) {
            rank[suffixes[i]] = i;
        }
        
        // 根据前k个字符的排名对后缀进行排序
        sort(suffixes.begin(), suffixes.end(), [&s, &rank, k, n](int a, int b) {
            if (rank[a] != rank[b]) {
                return rank[a] < rank[b];
            }
            int nextRankA = (a + k < n) ? rank[a + k] : -1;
            int nextRankB = (b + k < n) ? rank[b + k] : -1;
            return nextRankA < nextRankB;
        });
        
        k *= 2;
    }
    
    return suffixes;
}

/**
 * 根据后缀数组构建LCP数组
 * @param s 输入字符串
 * @param suffixArray 后缀数组
 * @return LCP数组
 */
vector<int> lcpArray(const string& s, const vector<int>& suffixArray) {
    int n = s.length();
    vector<int> rank(n);
    for (int i = 0; i < n; i++) {
        rank[suffixArray[i]] = i;
    }
    
    vector<int> lcp(n);
    int h = 0;
    
    for (int i = 0; i < n; i++) {
        if (rank[i] > 0) {
            int j = suffixArray[rank[i] - 1];
            while (i + h < n && j + h < n && s[i + h] == s[j + h]) {
                h++;
            }
            lcp[rank[i]] = h;
            if (h > 0) {
                h--;
            }
        }
    }
    
    return lcp;
}

/**
 * 解决String Function Calculation问题
 * @param s 输入字符串
 * @return f(S)的最大值
 */
long long solveStringFunctionCalculation(const string& s) {
    if (s.empty()) {
        return 0;
    }
    
    int n = s.length();
    
    // 特殊情况：所有字符相同
    bool allSame = true;
    for (int i = 1; i < n; i++) {
        if (s[i] != s[0]) {
            allSame = false;
            break;
        }
    }
    
    if (allSame) {
        // 对于n个相同字符，长度为k的子串出现次数为n-k+1
        // f(k) = k * (n-k+1)
        // 求最大值
        long long maxVal = 0;
        for (int k = 1; k <= n; k++) {
            long long val = (long long)k * (n - k + 1);
            maxVal = max(maxVal, val);
        }
        return maxVal;
    }
    
    // 构建后缀数组和LCP数组
    vector<int> sa = suffixArray(s);
    vector<int> lcp = lcpArray(s, sa);
    
    // 使用单调栈计算最大f值
    // 在LCP数组上使用单调栈，计算每个LCP值能贡献的最大f值
    stack<int> st;
    long long maxResult = n; // 至少有n个单字符子串，每个出现1次，f=1*n=n
    
    // 在LCP数组前后添加0，便于处理边界情况
    vector<int> extendedLcp(lcp.size() + 2);
    for (size_t i = 0; i < lcp.size(); i++) {
        extendedLcp[i + 1] = lcp[i];
    }
    
    for (size_t i = 0; i < extendedLcp.size(); i++) {
        // 维护单调递增栈
        while (!st.empty() && 
               (i == extendedLcp.size() - 1 || extendedLcp[st.top()] > extendedLcp[i])) {
            // 弹出栈顶元素，计算以该元素为最小值的区间的贡献
            int idx = st.top();
            st.pop();
            int height = extendedLcp[idx];
            
            // 计算区间的左右边界
            int left = st.empty() ? 0 : st.top() + 1;
            int right = i - 1;
            
            // 区间长度
            int width = right - left + 1;
            
            // 以height为长度的子串出现次数为width
            // f = height * width
            if (height > 0) {
                long long result = (long long)height * width;
                maxResult = max(maxResult, result);
            }
        }
        
        st.push(i);
    }
    
    return maxResult;
}

/**
 * 主函数
 */
int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    
    string line;
    getline(cin, line);
    
    if (line.empty()) {
        return 0;
    }
    
    // 求解并输出结果
    long long result = solveStringFunctionCalculation(line);
    cout << result << endl;
    
    return 0;
}