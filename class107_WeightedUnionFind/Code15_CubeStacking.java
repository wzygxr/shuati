package class156;

// Cube Stacking (POJ 1988)
// 有N个立方体积木，编号1~N，一开始每个积木单独成一列
// 实现如下两种操作，操作一共调用P次
// M x y : 将包含积木x的积木列整体移动到包含积木y的积木列的顶部
// C x : 查询积木x下方有多少个积木
// 1 <= N <= 30000, 1 <= P <= 100000
// 测试链接 : http://poj.org/problem?id=1988
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 带权并查集解决立方体积木叠放问题
 * 
 * 问题分析：
 * 维护立方体积木列的合并和查询操作，需要支持：
 * 1. 将一个积木列整体移动到另一个积木列顶部
 * 2. 查询某个积木下方的积木数量
 * 
 * 核心思想：
 * 1. 使用带权并查集维护积木之间的相对位置关系
 * 2. dist[i] 表示积木i到其所在积木列底部的距离（以积木数量为单位）
 * 3. size[i] 表示以积木i为根的积木列中积木的数量
 * 
 * 时间复杂度分析：
 * - prepare: O(n)
 * - find: O(α(n)) 近似O(1)
 * - union: O(α(n)) 近似O(1)
 * - query: O(α(n)) 近似O(1)
 * - 总体: O(n + P * α(n))
 * 
 * 空间复杂度: O(n) 用于存储father、dist和size数组
 * 
 * 应用场景：
 * - 积木叠放与查询
 * - 动态维护序列位置关系
 * - 游戏中的编队系统
 * 
 * 题目来源：POJ 1988
 * 题目链接：http://poj.org/problem?id=1988
 * 题目名称：Cube Stacking
 */
public class Code15_CubeStacking {

    public static int MAXN = 30001;

    public static int n = 30000;

    // father[i] 表示积木i的父节点
    public static int[] father = new int[MAXN];

    // dist[i] 表示积木i到其所在积木列底部的距离
    public static int[] dist = new int[MAXN];

    // size[i] 表示以积木i为根的积木列中积木的数量
    public static int[] size = new int[MAXN];

    // 递归会爆栈，所以用迭代来寻找并查集代表节点
    public static int[] stack = new int[MAXN];

    /**
     * 初始化并查集
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static void prepare() {
        // 初始化每个积木为自己所在积木列的代表
        for (int i = 1; i <= n; i++) {
            father[i] = i;
            // 初始时每个积木到积木列底部的距离为0
            dist[i] = 0;
            // 初始时每个积木列只有1个积木
            size[i] = 1;
        }
    }

    /**
     * 查找积木i所在积木列的代表（底部），并进行路径压缩
     * 同时更新dist[i]为积木i到积木列底部的距离
     * 使用迭代而非递归，避免栈溢出
     * 时间复杂度: O(α(n)) 近似O(1)
     * 
     * @param i 要查找的积木编号
     * @return 积木i所在积木列的代表（底部）
     */
    public static int find(int i) {
        // 使用栈模拟递归过程
        int si = 0;
        // 找到根节点
        while (i != father[i]) {
            stack[++si] = i;
            i = father[i];
        }
        stack[si + 1] = i;
        // 从根节点开始，向上更新距离
        for (int j = si; j >= 1; j--) {
            father[stack[j]] = i;
            // 更新距离：当前积木到积木列底部的距离 = 当前积木到父节点的距离 + 父节点到积木列底部的距离
            dist[stack[j]] += dist[stack[j + 1]];
        }
        return i;
    }

    /**
     * 合并两个积木列，将包含积木x的积木列整体移动到包含积木y的积木列顶部
     * 时间复杂度: O(α(n)) 近似O(1)
     * 
     * @param x 积木x的编号
     * @param y 积木y的编号
     */
    public static void union(int x, int y) {
        // 查找两个积木所在积木列的代表
        int xf = find(x), yf = find(y);
        // 如果不在同一积木列中
        if (xf != yf) {
            // 将包含积木x的积木列合并到包含积木y的积木列顶部
            father[xf] = yf;
            // 更新包含积木x的积木列底部到包含积木y的积木列底部的距离
            // 距离 = 包含积木y的积木列的积木数量（即包含积木y的积木列顶部到新积木列底部的距离）
            dist[xf] += size[yf];
            // 更新新积木列的积木数量
            size[yf] += size[xf];
        }
    }

    /**
     * 查询积木x下方的积木数量
     * 时间复杂度: O(α(n)) 近似O(1)
     * 
     * @param x 积木x的编号
     * @return 积木x下方的积木数量
     */
    public static int query(int x) {
        // 确保路径压缩完成
        find(x);
        // 积木x下方的积木数量 = 积木x到积木列底部的距离
        return dist[x];
    }

    public static void main(String[] args) throws IOException {
        prepare();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        int p = (int) in.nval;
        String op;
        // 处理P个操作
        for (int i = 1, x, y; i <= p; i++) {
            op = br.readLine().trim();
            String[] parts = op.split(" ");
            if (parts[0].equals("M")) {
                // 合并积木列
                x = Integer.parseInt(parts[1]);
                y = Integer.parseInt(parts[2]);
                union(x, y);
            } else {
                // 查询积木下方的积木数量
                x = Integer.parseInt(parts[1]);
                out.println(query(x));
            }
        }
        out.flush();
        out.close();
        br.close();
    }
}