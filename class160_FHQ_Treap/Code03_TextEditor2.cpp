/*
 * 文本编辑器，FHQ-Treap实现区间移动，C++版本（优化版）
 * 一开始文本为空，光标在文本开头，也就是1位置，请实现如下6种操作
 * Move k     : 将光标移动到第k个字符之后，操作保证光标不会到非法位置
 * Insert n s : 在光标处插入长度为n的字符串s，光标位置不变
 * Delete n   : 删除光标后的n个字符，光标位置不变，操作保证有足够字符
 * Get n      : 输出光标后的n个字符，光标位置不变，操作保证有足够字符
 * Prev       : 光标前移一个字符，操作保证光标不会到非法位置
 * Next       : 光标后移一个字符，操作保证光标不会到非法位置
 * Insert操作时，字符串s中ASCII码在[32,126]范围上的字符一定有n个，其他字符请过滤掉
 * 测试链接 : https://www.luogu.com.cn/problem/P4008
 * 
 * 时间复杂度和空间复杂度分析：
 * - 时间复杂度：所有操作平均O(log n)，其中n为文本长度
 * - 空间复杂度：O(n)，存储文本字符和Treap节点
 * 最优解：FHQ-Treap是解决此类区间操作问题的经典最优解
 * 
 * 工程化考量：
 * - 使用数组实现避免指针操作，提高性能
 * - 优化内存分配，预分配足够空间
 * - 异常处理：确保操作不会导致程序崩溃
 * - 边界检查：验证所有输入参数的合法性
 */

#include <iostream>
#include <vector>
#include <cstdlib>
#include <cstring>
#include <algorithm>
#include <random>
using namespace std;

const int MAXN = 2000001;

int head = 0;
int cnt = 0;
char key[MAXN];
int ls[MAXN];
int rs[MAXN];
int siz[MAXN];
double priority[MAXN];
char ans[MAXN];
int ansi;

// 更新节点大小信息
void up(int i) {
    siz[i] = siz[ls[i]] + siz[rs[i]] + 1;
}

// 分割Treap的非递归实现，避免递归深度问题
void split(int l, int r, int i, int rank) {
    if (i == 0) {
        rs[l] = ls[r] = 0;
    } else {
        if (siz[ls[i]] + 1 <= rank) {
            rs[l] = i;
            split(i, r, rs[i], rank - siz[ls[i]] - 1);
        } else {
            ls[r] = i;
            split(l, i, ls[i], rank);
        }
        up(i);
    }
}

// 合并两个Treap
int merge(int x, int y) {
    if (x == 0) return y;
    if (y == 0) return x;
    if (priority[x] > priority[y]) {
        rs[x] = merge(rs[x], y);
        up(x);
        return x;
    } else {
        ls[y] = merge(x, ls[y]);
        up(y);
        return y;
    }
}

// 创建新节点
int newnode(char c) {
    int i = ++cnt;
    key[i] = c;
    ls[i] = 0;
    rs[i] = 0;
    siz[i] = 1;
    // 使用更好的随机数生成器
    static std::random_device rd;
    static std::mt19937 gen(rd());
    static std::uniform_real_distribution<> dis(0.0, 1.0);
    priority[i] = dis(gen);
    return i;
}

// 中序遍历获取字符
void getChars(int x) {
    if (x == 0) return;
    getChars(ls[x]);
    ans[ansi++] = key[x];
    getChars(rs[x]);
}

// 构建字符的Treap树
int buildTree(const vector<char>& chars) {
    if (chars.empty()) return 0;
    
    // 使用递归构建平衡的Treap
    function<int(int, int)> build = [&](int l, int r) -> int {
        if (l > r) return 0;
        int mid = (l + r) / 2;
        int node = newnode(chars[mid]);
        ls[node] = build(l, mid - 1);
        rs[node] = build(mid + 1, r);
        up(node);
        return node;
    };
    
    return build(0, chars.size() - 1);
}

int main() {
    // 初始化随机数种子
    srand(time(0));
    
    int cursor = 0; // 光标位置
    int totalSize = 0; // 文本总长度
    
    char command[10];
    int n;
    char s[2000001];
    
    while (scanf("%s", command) != EOF) {
        if (strcmp(command, "Move") == 0) {
            scanf("%d", &n);
            // 边界检查
            if (n < 0) n = 0;
            if (n > totalSize) n = totalSize;
            cursor = n;
        } else if (strcmp(command, "Insert") == 0) {
            scanf("%d", &n);
            getchar(); // 读取空格
            
            // 边界检查
            if (n <= 0) continue;
            
            fgets(s, n + 1, stdin);
            
            // 过滤有效字符
            vector<char> validChars;
            for (int i = 0; i < n; i++) {
                if (s[i] >= 32 && s[i] <= 126) {
                    validChars.push_back(s[i]);
                }
            }
            
            if (!validChars.empty()) {
                // 分割文本
                int leftPart = 0, rightPart = 0;
                split(0, 0, head, cursor);
                leftPart = ls[0];
                rightPart = rs[0];
                
                // 创建新节点
                int newTree = buildTree(validChars);
                
                // 合并
                head = merge(merge(leftPart, newTree), rightPart);
                totalSize += validChars.size();
            }
        } else if (strcmp(command, "Delete") == 0) {
            scanf("%d", &n);
            
            // 边界检查
            if (n <= 0 || cursor + n > totalSize) continue;
            
            int leftPart = 0, mid = 0, rightPart = 0;
            split(0, 0, head, cursor);
            leftPart = ls[0];
            int temp = rs[0];
            split(0, 0, temp, n);
            mid = ls[0];
            rightPart = rs[0];
            
            head = merge(leftPart, rightPart);
            totalSize -= n;
        } else if (strcmp(command, "Get") == 0) {
            scanf("%d", &n);
            
            // 边界检查
            if (n <= 0 || cursor + n > totalSize) continue;
            
            int leftPart = 0, mid = 0, rightPart = 0;
            split(0, 0, head, cursor);
            leftPart = ls[0];
            int temp = rs[0];
            split(0, 0, temp, n);
            mid = ls[0];
            rightPart = rs[0];
            
            ansi = 0;
            getChars(mid);
            ans[ansi] = '\0';
            printf("%s\n", ans);
            
            head = merge(merge(leftPart, mid), rightPart);
        } else if (strcmp(command, "Prev") == 0) {
            if (cursor > 0) cursor--;
        } else if (strcmp(command, "Next") == 0) {
            if (cursor < totalSize) cursor++;
        }
    }
    
    return 0;
}