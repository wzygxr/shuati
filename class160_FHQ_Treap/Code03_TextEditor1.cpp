/*
 * 文本编辑器，FHQ-Treap实现区间移动，C++版本
 * 一开始文本为空，光标在文本开头，也就是1位置，请实现如下6种操作
 * Move k     : 将光标移动到第k个字符之后，操作保证光标不会到非法位置
 * Insert n s : 在光标处插入长度为n的字符串s，光标位置不变
 * Delete n   : 删除光标后的n个字符，光标位置不变，操作保证有足够字符
 * Get n      : 输出光标后的n个字符，光标位置不变，操作保证有足够字符
 * Prev       : 光标前移一个字符，操作保证光标不会到非法位置
 * Next       : 光标后移一个字符，操作保证光标不会到非法位置
 * Insert操作时，字符串s中ASCII码在[32,126]范围上的字符一定有n个，其他字符请过滤掉
 * 测试链接 : https://www.luogu.com.cn/problem/P4008
 * 时间复杂度和空间复杂度分析：
 * - 时间复杂度：所有操作平均O(log n)，其中n为文本长度
 * - 空间复杂度：O(n)，存储文本字符和Treap节点
 * 最优解：FHQ-Treap是解决此类区间操作问题的经典最优解
 */

#include <iostream>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <ctime>
#include <vector>
using namespace std;

const int MAXN = 2000001;

int head = 0;
int cnt = 0;
char key[MAXN];
int left_[MAXN];
int right_[MAXN];
int size_[MAXN];
double priority_[MAXN];
char ans[MAXN];
int ansi;

// 更新节点大小信息
void up(int i) {
    size_[i] = size_[left_[i]] + size_[right_[i]] + 1;
}

// 创建新节点
int newnode(char c) {
    int i = ++cnt;
    key[i] = c;
    left_[i] = 0;
    right_[i] = 0;
    size_[i] = 1;
    priority_[i] = (double)rand() / RAND_MAX;
    return i;
}

// 合并两个Treap
int merge(int x, int y) {
    if (x == 0) return y;
    if (y == 0) return x;
    if (priority_[x] > priority_[y]) {
        right_[x] = merge(right_[x], y);
        up(x);
        return x;
    } else {
        left_[y] = merge(x, left_[y]);
        up(y);
        return y;
    }
}

// 按大小分割Treap
pair<int, int> split(int x, int k) {
    if (x == 0) return {0, 0};
    if (k <= size_[left_[x]]) {
        auto [a, b] = split(left_[x], k);
        left_[x] = b;
        up(x);
        return {a, x};
    } else {
        auto [a, b] = split(right_[x], k - size_[left_[x]] - 1);
        right_[x] = a;
        up(x);
        return {x, b};
    }
}

// 中序遍历获取字符
void getChars(int x) {
    if (x == 0) return;
    getChars(left_[x]);
    ans[ansi++] = key[x];
    getChars(right_[x]);
}

int main() {
    srand(time(0));
    
    int cursor = 0; // 光标位置
    int totalSize = 0; // 文本总长度
    
    char command[10];
    int n;
    char s[2000001];
    
    while (scanf("%s", command) != EOF) {
        if (strcmp(command, "Move") == 0) {
            scanf("%d", &n);
            cursor = n;
        } else if (strcmp(command, "Insert") == 0) {
            scanf("%d", &n);
            getchar(); // 读取空格
            fgets(s, n + 1, stdin);
            
            // 过滤有效字符
            int validLen = 0;
            for (int i = 0; i < n; i++) {
                if (s[i] >= 32 && s[i] <= 126) {
                    s[validLen++] = s[i];
                }
            }
            
            if (validLen > 0) {
                // 分割文本
                auto [leftPart, rightPart] = split(head, cursor);
                
                // 创建新节点
                int newTree = 0;
                for (int i = 0; i < validLen; i++) {
                    newTree = merge(newTree, newnode(s[i]));
                }
                
                // 合并
                head = merge(merge(leftPart, newTree), rightPart);
                totalSize += validLen;
            }
        } else if (strcmp(command, "Delete") == 0) {
            scanf("%d", &n);
            
            auto [leftPart, temp] = split(head, cursor);
            auto [mid, rightPart] = split(temp, n);
            
            head = merge(leftPart, rightPart);
            totalSize -= n;
        } else if (strcmp(command, "Get") == 0) {
            scanf("%d", &n);
            
            auto [leftPart, temp] = split(head, cursor);
            auto [mid, rightPart] = split(temp, n);
            
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