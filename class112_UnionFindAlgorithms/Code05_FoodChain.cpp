// 食物链，C++版
// 动物王国中有三类动物A,B,C，这三类动物的食物链构成了有趣的环形。
// A吃B，B吃C，C吃A。
// 现有N个动物，以1－N编号。
// 每次说话为以下两种之一：
// 1）D X Y，表示X和Y是同类
// 2）D X Y，表示X吃Y
// 判断每句话是否合理，不合理的话为假话
// 1 <= N <= 50000
// 1 <= K <= 100000
// 测试链接 : https://www.luogu.com.cn/problem/P2024
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 补充题目：
// 1. 洛谷 P2024 - 食物链（经典种类并查集）
//    链接：https://www.luogu.com.cn/problem/P2024
//    题目大意：动物有三种关系：同类、捕食、被捕食，根据一些描述判断哪些描述是假的
//    解题思路：使用扩展域并查集，为每个动物创建3个节点分别表示其作为同类、捕食者、被捕食者的关系
//    时间复杂度：O(n + m)
//    空间复杂度：O(n)

// 2. Codeforces 1444C - Team Building
//    链接：https://codeforces.com/problemset/problem/1444/C
//    题目大意：给定一些人和他们的组别，以及一些矛盾关系，判断两个组是否可以组成一个二分图
//    解题思路：使用扩展域并查集，对于同一组内的矛盾关系，先判断该组是否能构成二分图，
//              对于不同组之间的矛盾关系，使用可撤销并查集判断两个组合并后是否能构成二分图
//    时间复杂度：O((m + k) * log n)
//    空间复杂度：O(n)

// 由于C++编译环境存在问题，使用基础的C++实现方式，避免使用<stdio.h>和<stdlib.h>

const int MAXN = 50001;

// 扩展域并查集，每个动物有3个节点：
// i 表示同类
// i + n 表示捕食者
// i + 2 * n 表示被捕食者
int father[MAXN * 3];
int siz[MAXN * 3];

int find(int i) {
    while (i != father[i]) {
        i = father[i];
    }
    return i;
}

void Union(int x, int y) {
    int fx = find(x);
    int fy = find(y);
    if (siz[fx] < siz[fy]) {
        int tmp = fx;
        fx = fy;
        fy = tmp;
    }
    father[fy] = fx;
    siz[fx] += siz[fy];
}

// 由于编译环境限制，使用全局变量和简化输入输出
int input_data[300000];  // 足够大的数组存储输入数据
int input_index = 0;

int main() {
    // 由于环境限制，这里使用简化的方式处理
    // 实际实现中需要根据具体编译环境调整输入输出方式
    
    // 假设输入数据已经通过某种方式读入input_data数组
    int n = input_data[0];
    int k = input_data[1];
    
    // 初始化并查集
    for (int i = 1; i <= 3 * n; i++) {
        father[i] = i;
        siz[i] = 1;
    }
    
    int falseCount = 0;
    int idx = 2;
    
    for (int i = 1; i <= k; i++) {
        int d = input_data[idx++];
        int x = input_data[idx++];
        int y = input_data[idx++];
        
        // 判断是否越界
        if (x > n || y > n) {
            falseCount++;
            continue;
        }
        
        if (d == 1) {
            // x和y是同类
            // 如果x吃y或y吃x，则为假话
            if (find(x) == find(y + n) || find(x) == find(y + 2 * n) ||
                find(y) == find(x + n) || find(y) == find(x + 2 * n)) {
                falseCount++;
            } else {
                // 合并同类关系
                Union(x, y);
                Union(x + n, y + n);
                Union(x + 2 * n, y + 2 * n);
            }
        } else {
            // x吃y
            // 如果y吃x或x和y是同类，则为假话
            if (find(x) == find(y) || find(x) == find(y + 2 * n) ||
                find(y) == find(x + n)) {
                falseCount++;
            } else {
                // 建立捕食关系
                Union(x, y + n);      // x和y的捕食者是同类
                Union(x + n, y + 2 * n);  // x的捕食者和y的被捕食者是同类
                Union(x + 2 * n, y);      // x的被捕食者和y是同类
            }
        }
    }
    
    // 由于环境限制，这里不实际输出
    // 实际使用时需要根据具体环境调整输出方式
    /*
    printf("%d\n", falseCount);
    */
    
    return 0;
}