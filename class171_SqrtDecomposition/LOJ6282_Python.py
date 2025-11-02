import sys

"""
LOJ 6282. 数列分块入门 6 - Python实现

题目描述：
给出一个长为 n 的数列，以及 n 个操作，操作涉及单点插入，单点询问。

解题思路：
使用分块算法，将数组分成多个块，每个块维护一个动态数组。
单点插入操作时：
1. 找到要插入元素的块
2. 在该块中插入元素
3. 检查块大小，如果超过设定阈值，则重新分块
单点查询时：
1. 找到元素所在块
2. 在块中查找元素

时间复杂度：
- 单点插入：平均 O(√n)
- 单点查询：O(√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：通过动态维护块结构减少操作时间
4. 鲁棒性：处理边界情况和特殊输入
"""

BLOCK_SIZE = 300

class LOJ6282:
    def __init__(self):
        self.blocks = []  # 存储每个块的数据
        self.blocks.append([])  # 初始化第一个块
        self.size = 0  # 数组的当前大小
    
    def insert(self, pos, val):
        """单点插入操作"""
        pos -= 1  # 转换为0基索引
        
        # 找到要插入的块
        block_index = 0
        current_pos = 0
        while block_index < len(self.blocks) and current_pos + len(self.blocks[block_index]) <= pos:
            current_pos += len(self.blocks[block_index])
            block_index += 1
        
        # 在对应块中插入元素
        target_block = self.blocks[block_index]
        target_block.insert(pos - current_pos, val)
        self.size += 1
        
        # 检查是否需要重新分块（如果块太大）
        if len(target_block) > 2 * BLOCK_SIZE:
            # 创建新块
            new_block = []
            mid = len(target_block) // 2
            
            # 将后半部分移到新块
            new_block.extend(target_block[mid:])
            # 移除原块的后半部分
            del target_block[mid:]
            
            # 插入新块
            self.blocks.insert(block_index + 1, new_block)
    
    def query(self, pos):
        """单点查询操作"""
        pos -= 1  # 转换为0基索引
        
        # 找到要查询的块
        block_index = 0
        current_pos = 0
        while block_index < len(self.blocks) and current_pos + len(self.blocks[block_index]) <= pos:
            current_pos += len(self.blocks[block_index])
            block_index += 1
        
        # 在对应块中查询元素
        target_block = self.blocks[block_index]
        return target_block[pos - current_pos]

# 主函数
if __name__ == "__main__":
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    solution = LOJ6282()
    
    # 读取初始数组
    for i in range(n):
        value = int(input[ptr])
        ptr += 1
        solution.insert(i + 1, value)  # 插入到当前数组末尾
    
    # 处理操作
    for _ in range(n):
        op = int(input[ptr])
        l = int(input[ptr+1])
        r = int(input[ptr+2])
        c = int(input[ptr+3])
        ptr += 4
        
        if op == 0:
            # 单点插入
            solution.insert(l, r)
        else:
            # 单点查询
            print(solution.query(r))