// 扫描线问题 - 分块算法实现 (C++版本)
// 题目来源: HDU 1542
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1542
// 题目大意: 计算多个矩形覆盖的总面积
// 约束条件: 矩形数量n ≤ 100

#include <cstdio>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

const int MAXN = 205;
const int BLOCK_SIZE = 15; // 约等于sqrt(200)

struct Line {
    double x, y1, y2; // 扫描线的x坐标，y范围
    int flag; // 1表示进入，-1表示离开
    Line(double x = 0, double y1 = 0, double y2 = 0, int flag = 0) : 
        x(x), y1(y1), y2(y2), flag(flag) {}
    bool operator < (const Line& other) const {
        return x < other.x;
    }
} lines[MAXN];

double y_coords[MAXN]; // 存储所有y坐标，用于离散化
int n, m; // n是矩形数量，m是y坐标的数量

// 分块数据结构
int blen; // 块的大小
int block_count; // 块的数量
int cover[MAXN]; // 每个位置被覆盖的次数
int block_cover[MAXN]; // 每个块的覆盖标记（延迟更新）

// 离散化y坐标
void discretize_y() {
    sort(y_coords, y_coords + m);
    m = unique(y_coords, y_coords + m) - y_coords;
}

// 初始化分块结构
void init_blocks() {
    blen = sqrt(m - 1);
    if (blen == 0) blen = 1;
    block_count = (m - 1 + blen - 1) / blen;
    
    // 初始化覆盖数组
    for (int i = 0; i < m - 1; i++) {
        cover[i] = 0;
    }
    for (int i = 0; i < block_count; i++) {
        block_cover[i] = 0;
    }
}

// 更新区间覆盖
void update_range(int l, int r, int delta) {
    int left_block = l / blen;
    int right_block = r / blen;
    
    if (left_block == right_block) {
        // 所有元素都在同一个块内，直接暴力更新
        for (int i = l; i <= r; i++) {
            cover[i] += delta;
        }
    } else {
        // 处理左边不完整的块
        for (int i = l; i < (left_block + 1) * blen; i++) {
            cover[i] += delta;
        }
        
        // 处理中间完整的块（使用块标记）
        for (int i = left_block + 1; i < right_block; i++) {
            block_cover[i] += delta;
        }
        
        // 处理右边不完整的块
        for (int i = right_block * blen; i <= r; i++) {
            cover[i] += delta;
        }
    }
}

// 计算当前覆盖的总长度
double calculate_covered_length() {
    double total = 0;
    
    for (int i = 0; i < m - 1; i++) {
        int block_idx = i / blen;
        int total_cover = cover[i] + block_cover[block_idx];
        
        if (total_cover > 0) {
            total += y_coords[i + 1] - y_coords[i];
        }
    }
    
    return total;
}

// 主函数，计算矩形覆盖的总面积
double solve() {
    // 离散化y坐标
    discretize_y();
    
    // 初始化分块结构
    init_blocks();
    
    // 按照x坐标排序扫描线
    sort(lines, lines + 2 * n);
    
    double area = 0;
    for (int i = 0; i < 2 * n - 1; i++) {
        // 找到y1和y2在离散化数组中的位置
        int y1_pos = lower_bound(y_coords, y_coords + m, lines[i].y1) - y_coords;
        int y2_pos = lower_bound(y_coords, y_coords + m, lines[i].y2) - y_coords;
        
        // 更新覆盖区间
        update_range(y1_pos, y2_pos - 1, lines[i].flag);
        
        // 计算当前扫描线到下一条扫描线之间的面积
        double current_length = calculate_covered_length();
        double delta_x = lines[i + 1].x - lines[i].x;
        area += current_length * delta_x;
    }
    
    return area;
}

int main() {
    int case_num = 0;
    while (scanf("%d", &n) && n) {
        case_num++;
        m = 0;
        
        // 读取每个矩形
        for (int i = 0; i < n; i++) {
            double x1, y1, x2, y2;
            scanf("%lf %lf %lf %lf", &x1, &y1, &x2, &y2);
            
            // 添加两条扫描线
            lines[2 * i] = Line(x1, y1, y2, 1);
            lines[2 * i + 1] = Line(x2, y1, y2, -1);
            
            // 收集y坐标
            y_coords[m++] = y1;
            y_coords[m++] = y2;
        }
        
        double area = solve();
        printf("Test case #%d\nTotal explored area: %.2f\n\n", case_num, area);
    }
    
    return 0;
}

/*
时间复杂度分析：
- 离散化：O(n log n)
- 初始化分块：O(n)
- 扫描线排序：O(n log n)
- 每次更新操作：O(√n)
- 每次长度计算：O(n)
- 总体时间复杂度：O(n^2 + n√n)

空间复杂度分析：
- 存储扫描线：O(n)
- 存储y坐标：O(n)
- 分块数据结构：O(n)
- 总体空间复杂度：O(n)

算法说明：
扫描线算法是解决矩形覆盖问题的经典算法，结合分块可以更高效地处理区间更新操作。

算法步骤：
1. 离散化所有y坐标，减少处理范围
2. 将每个矩形转换为两条扫描线（左边界+1，右边界-1）
3. 按照x坐标排序所有扫描线
4. 从左到右扫描，维护当前的垂直覆盖情况
5. 对于每两条相邻的扫描线，计算当前覆盖的垂直长度乘以水平距离，累加到总面积中

优化说明：
1. 使用分块优化区间更新操作，将时间复杂度从O(n)降低到O(√n)
2. 使用离散化处理浮点数坐标问题
3. 使用延迟更新技术减少不必要的操作

与其他方法的对比：
- 线段树：时间复杂度更低（O(n log n)），但实现更复杂
- 暴力法：时间复杂度O(n^2)，对于小数据也可接受
- 分块算法：实现相对简单，时间复杂度适中

工程化考虑：
1. 注意浮点数精度问题，使用double类型存储坐标
2. 离散化是处理浮点数的关键步骤
3. 扫描线的处理需要注意边界情况
4. 分块的大小选择可以根据具体数据规模调整
*/