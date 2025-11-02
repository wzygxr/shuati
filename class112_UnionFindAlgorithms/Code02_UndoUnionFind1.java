package class165;

// 可撤销并查集模版题，java版
// 一共有n个点，每个点有两个小球，每个点给定两个小球的编号
// 一共有n-1条无向边，所有节点连成一棵树
// 对i号点，2 <= i <= n，都计算如下问题的答案并打印
// 从1号点到i号点的最短路径上，每个点只能拿一个小球，最多能拿几个编号不同的小球
// 1 <= n <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/AT_abc302_h
// 测试链接 : https://atcoder.jp/contests/abc302/tasks/abc302_h
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

// 相关题目及解析：

// 1. AtCoder ABC302 H - Ball Collector
//    链接：https://atcoder.jp/contests/abc302/tasks/abc302_h
//    题目大意：在一棵树上，每个节点有两个球，要求从根节点到每个节点的路径上收集球，使得收集的球编号各不相同
//    解题思路：使用可撤销并查集维护连通性，在DFS过程中合并节点，回溯时撤销操作
//    时间复杂度：O(n log n)
//    空间复杂度：O(n)
//    实现细节：
//    - 每个球作为并查集中的一个节点
//    - 对于每个节点，将其两个球合并到当前路径的集合中
//    - 使用DFS遍历树，进入节点时执行合并，离开时执行撤销
//    - 使用edgeCnt数组记录每个集合中的边数，当边数小于节点数时可以添加一个新球

// 2. Codeforces 891C - Envy
//    链接：https://codeforces.com/problemset/problem/891/C
//    题目大意：给定一个图和一些边的集合，判断这些边是否可以同时出现在一个最小生成树中
//    解题思路：使用可撤销并查集，按照Kruskal算法的思想，先加入权重小于当前查询边的边，然后尝试加入查询的边，如果会形成环则不能同时出现在MST中
//    时间复杂度：O(m log m + q * k * log n)
//    空间复杂度：O(n + m)
//    实现细节：
//    - 将所有边按权值排序
//    - 将查询按最大边权分组
//    - 对于每组查询，先加入所有权值小于查询组最大边权的边
//    - 对查询组内的边，尝试用可撤销并查集合并，如果有环则该查询不可行
//    - 处理完查询后撤销合并操作

// 3. Codeforces 1681F - Unique Occurrences
//    链接：https://codeforces.com/problemset/problem/1681/F
//    题目大意：在树上处理路径查询问题，统计某些路径上唯一出现的颜色数量
//    解题思路：可以使用可撤销并查集维护路径的连通性信息
//    时间复杂度：O(n log n)
//    空间复杂度：O(n)
//    实现细节：
//    - 使用离线处理方法，将查询按右端点排序
//    - 使用颜色首次出现的位置记录
//    - 使用可撤销并查集维护区间内的连通性

// 4. Codeforces 1291F - Coffee Varieties (hard version)
//    链接：https://codeforces.com/problemset/problem/1291/F
//    题目大意：交互题，需要通过特定操作识别咖啡品种
//    解题思路：可以使用可撤销并查集维护品种的等价关系
//    时间复杂度：O(n log n)
//    空间复杂度：O(n)
//    实现细节：
//    - 使用可撤销并查集记录品种之间的等价关系
//    - 根据交互结果动态调整等价关系
//    - 利用可撤销操作回溯到之前的状态

// 5. Codeforces 915F - Imbalance Value of a Tree
//    链接：https://codeforces.com/problemset/problem/915/F
//    题目大意：计算树中所有路径的最大值与最小值之差的和
//    解题思路：使用可撤销并查集，按边权排序后逐步合并，统计贡献
//    时间复杂度：O(n log n)
//    空间复杂度：O(n)
//    实现细节：
//    - 将边分别按权值升序和降序排序
//    - 使用可撤销并查集统计不同权值范围内的连通块大小
//    - 通过容斥原理计算所有路径的贡献

// 思路技巧总结：
// 1. 可撤销并查集适用于需要回溯状态的场景，特别是DFS等需要回退操作的算法
// 2. 实现关键是记录每次合并操作的状态变化，通常使用栈来保存
// 3. 不能使用路径压缩优化，因为路径压缩会破坏合并历史，无法正确撤销
// 4. 必须使用按秩合并（size/rank）来保证查询效率
// 5. 撤销操作的时间复杂度为O(1)，但需要确保栈的空间足够

// 工程化考量：
// 1. 栈空间管理：需要根据最大可能操作次数合理设置栈的大小
// 2. 异常处理：需要处理栈空、重复撤销等异常情况
// 3. 性能优化：在大规模数据下，合理使用非递归实现以减少函数调用开销
// 4. 可扩展性：可以设计成通用的模板类，支持不同类型的元素
// 5. 内存管理：对于频繁的撤销操作，注意内存的及时回收

// 跨语言实现注意事项：
// 1. Java中使用数组实现栈时要注意初始容量的设置
// 2. C++中可以使用vector或stack容器来管理撤销操作
// 3. Python中可以使用列表来模拟栈，注意内存效率
// 4. 不同语言的整数范围可能不同，需要注意溢出问题

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/*
 * C++版本实现
#include <iostream>
#include <vector>
#include <stack>
using namespace std;

struct UndoUnionFind {
    vector<int> parent;  // 存储每个节点的父节点
    vector<int> rank;    // 存储每个节点的秩（树高的上界）
    struct Operation {   // 记录合并操作的结构
        int x;           // 被合并的节点
        int px;          // 合并前x的父节点
        int y;           // 被合并的节点
        int py;          // 合并前y的父节点
        int r;           // 合并前的秩
    };
    stack<Operation> stk; // 存储合并操作的栈
    
    // 构造函数，初始化每个节点为单独的集合
    UndoUnionFind(int n) {
        parent.resize(n + 1);
        rank.resize(n + 1, 1);
        // 初始化每个节点的父节点为自己
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
    }
    
    // 查找操作，查找x所在集合的根节点（不使用路径压缩）
    int find(int x) {
        if (parent[x] != x) {
            return find(parent[x]);
        }
        return x;
    }
    
    // 合并操作，将x和y所在的集合合并
    void unite(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        
        if (fx != fy) {
            // 按秩合并，将秩小的树合并到秩大的树下
            if (rank[fx] > rank[fy]) {
                swap(fx, fy);
            }
            // 记录操作前的状态
            Operation op = {fx, parent[fx], fy, parent[fy], rank[fy]};
            stk.push(op);
            // 执行合并
            parent[fx] = fy;
            if (rank[fx] == rank[fy]) {
                rank[fy]++;
            }
        }
    }
    
    // 撤销操作，撤销最近的一次合并
    void undo() {
        if (!stk.empty()) {
            Operation op = stk.top();
            stk.pop();
            // 恢复父节点和秩
            parent[op.x] = op.px;
            parent[op.y] = op.py;
            rank[op.y] = op.r;
        }
    }
    
    // 获取栈的大小，用于判断有多少操作可以撤销
    int size() {
        return stk.size();
    }
};

// 使用示例：处理AtCoder ABC302 H题的简化版本
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    UndoUnionFind uf(n);
    
    // 处理一些操作...
    // 合并操作
    uf.unite(1, 2);
    uf.unite(3, 4);
    
    // 撤销最近的操作
    uf.undo();
    
    return 0;
}
*/

/*
 * Python版本实现
class UndoUnionFind:
    def __init__(self, n):
        # 初始化每个节点的父节点为自己，秩为1
        self.parent = list(range(n + 1))
        self.rank = [1] * (n + 1)
        # 栈用于记录合并操作
        self.stack = []
    
    def find(self, x):
        """查找x所在集合的根节点（不使用路径压缩）"""
        while self.parent[x] != x:
            x = self.parent[x]
        return x
    
    def unite(self, x, y):
        """合并x和y所在的集合，记录操作以便撤销"""
        fx = self.find(x)
        fy = self.find(y)
        
        if fx != fy:
            # 按秩合并
            if self.rank[fx] > self.rank[fy]:
                fx, fy = fy, fx
            # 记录操作前的状态
            self.stack.append({
                'x': fx,
                'px': self.parent[fx],
                'y': fy,
                'py': self.parent[fy],
                'r': self.rank[fy]
            })
            # 执行合并
            self.parent[fx] = fy
            if self.rank[fx] == self.rank[fy]:
                self.rank[fy] += 1
    
    def undo(self):
        """撤销最近的一次合并操作"""
        if self.stack:
            op = self.stack.pop()
            # 恢复父节点和秩
            self.parent[op['x']] = op['px']
            self.parent[op['y']] = op['py']
            self.rank[op['y']] = op['r']
    
    def size(self):
        """返回栈的大小，即可以撤销的操作次数"""
        return len(self.stack)

# 使用示例
def main():
    import sys
    input = sys.stdin.read().split()
    idx = 0
    
    n = int(input[idx])
    idx += 1
    
    uf = UndoUnionFind(n)
    
    # 处理一些操作...
    # 这里可以根据具体问题添加处理逻辑
    
    # 示例操作
    uf.unite(1, 2)
    uf.unite(3, 4)
    uf.undo()  # 撤销第二个合并

if __name__ == "__main__":
    main()
*/

public class Code02_UndoUnionFind1 {

	public static int MAXN = 200001;
	public static int[][] arr = new int[MAXN][2];

	public static int[] head = new int[MAXN];
	public static int[] next = new int[MAXN << 1];
	public static int[] to = new int[MAXN << 1];
	public static int cnt;

	public static int[] father = new int[MAXN];
	public static int[] siz = new int[MAXN];
	public static int[] edgeCnt = new int[MAXN];

	public static int[][] rollback = new int[MAXN][2];
	public static int opsize = 0;

	public static int[] ans = new int[MAXN];
	public static int ball = 0;

	public static void addEdge(int u, int v) {
		next[++cnt] = head[u];
		to[cnt] = v;
		head[u] = cnt;
	}

	public static int find(int i) {
		while (i != father[i]) {
			i = father[i];
		}
		return i;
	}

	public static void union(int x, int y) {
		int fx = find(x);
		int fy = find(y);
		if (siz[fx] < siz[fy]) {
			int tmp = fx;
			fx = fy;
			fy = tmp;
		}
		father[fy] = fx;
		siz[fx] += siz[fy];
		edgeCnt[fx] += edgeCnt[fy] + 1;
		rollback[++opsize][0] = fx;
		rollback[opsize][1] = fy;
	}

	public static void undo() {
		int fx = rollback[opsize][0];
		int fy = rollback[opsize--][1];
		father[fy] = fy;
		siz[fx] -= siz[fy];
		edgeCnt[fx] -= edgeCnt[fy] + 1;
	}

	public static void dfs(int u, int fa) {
		int fx = find(arr[u][0]);
		int fy = find(arr[u][1]);
		boolean added = false;
		boolean unioned = false;
		if (fx == fy) {
			if (edgeCnt[fx] < siz[fx]) {
				ball++;
				added = true;
			}
			edgeCnt[fx]++;
		} else {
			if (edgeCnt[fx] < siz[fx] || edgeCnt[fy] < siz[fy]) {
				ball++;
				added = true;
			}
			union(fx, fy);
			unioned = true;
		}
		ans[u] = ball;
		for (int e = head[u]; e > 0; e = next[e]) {
			if (to[e] != fa) {
				dfs(to[e], u);
			}
		}
		if (added) {
			ball--;
		}
		if (unioned) {
			undo();
		} else {
			edgeCnt[fx]--;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			arr[i][0] = (int) in.nval;
			in.nextToken();
			arr[i][1] = (int) in.nval;
		}
		for (int i = 1, u, v; i < n; i++) {
			in.nextToken();
			u = (int) in.nval;
			in.nextToken();
			v = (int) in.nval;
			addEdge(u, v);
			addEdge(v, u);
		}
		for (int i = 1; i <= n; i++) {
			father[i] = i;
			siz[i] = 1;
			edgeCnt[i] = 0;
		}
		dfs(1, 0);
		for (int i = 2; i < n; i++) {
			out.print(ans[i] + " ");
		}
		out.println(ans[n]);
		out.flush();
		out.close();
		br.close();
	}

}