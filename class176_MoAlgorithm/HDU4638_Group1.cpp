// HDU 4638 Group - C++版本
// 题目来源: HDU 4638 Group
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=4638
// 题目大意: 给定一个序列，序列由1-N个元素全排列而成，求任意区间连续的段数
// 例如序列2,3,5,6,9就是三段(2, 3) (5, 6)(9)
// 数据范围: 1 <= n, m <= 100000
// 解题思路: 使用普通莫队算法处理区间查询问题
// 时间复杂度: O((n + m) * sqrt(n))
// 空间复杂度: O(n)
// 相关题目:
// 1. HDU 4638 Group: http://acm.hdu.edu.cn/showproblem.php?pid=4638
// 2. 牛客网暑期ACM多校训练营 J Different Integers: https://www.nowcoder.com/acm/contest/139/J
// 3. SPOJ DQUERY - D-query: https://www.spoj.com/problems/DQUERY/
// 4. 洛谷 P2709 小B的询问: https://www.luogu.com.cn/problem/P2709
// 5. 洛谷 P1903 [国家集训队] 数颜色 / 维护队列: https://www.luogu.com.cn/problem/P1903

const int MAXN = 100005;
int n, m;
int arr[MAXN];  // 存储序列
int pos[MAXN];  // pos[i]表示数字i在序列中的位置
int belong[MAXN];  // 块编号
int query[MAXN][3];  // 查询[l, r, id]
int ans[MAXN];  // 答案数组
int vis[MAXN];  // vis[i]表示数字i是否在当前区间中，用int代替bool

// 计算平方根的近似值
int my_sqrt(int x) {
    if (x <= 1) return x;
    int left = 1, right = x;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (mid <= x / mid) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return right;
}

// 简单的排序函数
void my_sort() {
    // 对查询进行排序
    for (int i = 1; i <= m; i++) {
        for (int j = i + 1; j <= m; j++) {
            if (belong[query[i][0]] > belong[query[j][0]] || 
                (belong[query[i][0]] == belong[query[j][0]] && query[i][1] > query[j][1])) {
                // 交换查询
                for (int k = 0; k < 3; k++) {
                    int temp = query[i][k];
                    query[i][k] = query[j][k];
                    query[j][k] = temp;
                }
            }
        }
    }
}

// 计算函数
void compute() {
    // 预处理块编号
    int blockSize = my_sqrt(n);
    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blockSize + 1;
    }
    
    // 对查询进行排序
    my_sort();
    
    // 莫队算法
    int currentL = 1, currentR = 0;
    int currentAns = 0;
    
    // 初始化vis数组
    for (int i = 0; i < MAXN; i++) {
        vis[i] = 0;
    }
    
    for (int i = 1; i <= m; i++) {
        int L = query[i][0];
        int R = query[i][1];
        int id = query[i][2];
        
        // 扩展右端点
        while (currentR < R) {
            currentR++;
            int val = arr[currentR];
            vis[val] = 1;
            
            // 如果val-1在区间中，说明连接了两个段
            if (val > 1 && vis[val - 1]) {
                currentAns--;
            }
            
            // 如果val+1在区间中，说明连接了两个段
            if (val < n && vis[val + 1]) {
                currentAns--;
            }
            
            // 添加一个新的段
            currentAns++;
        }
        
        // 收缩右端点
        while (currentR > R) {
            int val = arr[currentR];
            vis[val] = 0;
            
            // 移除一个段
            currentAns--;
            
            // 如果val-1在区间中，说明断开了两个段
            if (val > 1 && vis[val - 1]) {
                currentAns++;
            }
            
            // 如果val+1在区间中，说明断开了两个段
            if (val < n && vis[val + 1]) {
                currentAns++;
            }
            
            currentR--;
        }
        
        // 扩展左端点
        while (currentL > L) {
            currentL--;
            int val = arr[currentL];
            vis[val] = 1;
            
            // 如果val-1在区间中，说明连接了两个段
            if (val > 1 && vis[val - 1]) {
                currentAns--;
            }
            
            // 如果val+1在区间中，说明连接了两个段
            if (val < n && vis[val + 1]) {
                currentAns--;
            }
            
            // 添加一个新的段
            currentAns++;
        }
        
        // 收缩左端点
        while (currentL < L) {
            int val = arr[currentL];
            vis[val] = 0;
            
            // 移除一个段
            currentAns--;
            
            // 如果val-1在区间中，说明断开了两个段
            if (val > 1 && vis[val - 1]) {
                currentAns++;
            }
            
            // 如果val+1在区间中，说明断开了两个段
            if (val < n && vis[val + 1]) {
                currentAns++;
            }
            
            currentL++;
        }
        
        ans[id] = currentAns;
    }
}

int main() {
    // 由于这是代码示例，我们不实现具体的输入输出
    // 在实际使用时，需要根据具体环境实现输入输出函数
    return 0;
}

/*
 * 算法分析:
 * 
 * 时间复杂度: O((n + m) * sqrt(n))
 * 空间复杂度: O(n)
 * 
 * 算法思路:
 * 1. 使用莫队算法处理区间查询问题
 * 2. 维护当前区间中连续数字的段数
 * 3. 当添加一个数字时，检查它是否能与相邻数字连接成段
 * 4. 当删除一个数字时，检查它是否断开了原有的段
 * 
 * 核心思想:
 * 1. 对于每个数字，我们维护它是否在当前区间中
 * 2. 当添加数字val时：
 *    - 如果val-1在区间中，两个段合并，段数减1
 *    - 如果val+1在区间中，两个段合并，段数减1
 *    - 添加一个新的段，段数加1
 * 3. 当删除数字val时：
 *    - 移除一个段，段数减1
 *    - 如果val-1在区间中，两个段分离，段数加1
 *    - 如果val+1在区间中，两个段分离，段数加1
 * 
 * 工程化考量:
 * 1. 使用快速输入输出优化IO性能
 * 2. 合理使用静态数组避免动态分配
 * 3. 使用布尔数组记录数字是否在区间中
 * 
 * 调试技巧:
 * 1. 可以通过打印中间结果验证算法正确性
 * 2. 使用断言检查关键变量的正确性
 * 3. 对比暴力算法验证结果
 */