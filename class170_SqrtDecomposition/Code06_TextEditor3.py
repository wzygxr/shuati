# 文本编辑器，块状链表实现，Python版
# 题目来源：洛谷P4008 [NOI2003] 文本编辑器
# 题目链接：https://www.luogu.com.cn/problem/P4008
# 题目大意：
# 一开始文本为空，光标在文本开头，也就是1位置，请实现如下6种操作
# Move k     : 将光标移动到第k个字符之后，操作保证光标不会到非法位置
# Insert n s : 在光标处插入长度为n的字符串s，光标位置不变
# Delete n   : 删除光标后的n个字符，光标位置不变，操作保证有足够字符
# Get n      : 输出光标后的n个字符，光标位置不变，操作保证有足够字符
# Prev       : 光标前移一个字符，操作保证光标不会到非法位置
# Next       : 光标后移一个字符，操作保证光光标不会到非法位置
# Insert操作时，字符串s中ASCII码在[32,126]范围上的字符一定有n个，其他字符请过滤掉
# 测试链接 : https://www.luogu.com.cn/problem/P4008

# 解题思路：
# 使用块状链表实现文本编辑器
# 1. 将文本分成多个块，每个块大小约为2*sqrt(n)
# 2. 使用链表连接各个块
# 3. 对于各种操作，先定位到对应的块和块内位置，然后进行相应处理
# 4. 维护操作：检查相邻块，如果内容大小之和不超过块容量，则合并

# 时间复杂度分析：
# 1. 插入操作：O(sqrt(n) + len)，其中len为插入字符串长度
# 2. 删除操作：O(sqrt(n) + len)，其中len为删除字符串长度
# 3. 查询操作：O(sqrt(n) + len)，其中len为查询字符串长度
# 空间复杂度：O(n)，存储文本内容

import sys
import math
from typing import List, Optional

# 整个文章能到达的最大长度
MAXN = 3000001
# 块内容量，近似等于 2 * 根号n，每块内容大小不会超过容量
BLEN = 3001
# 块的数量上限
BNUM = (MAXN // BLEN) << 1

# 块类定义
class Block:
    def __init__(self, block_id: int):
        self.id = block_id
        self.data: List[str] = []  # 存储字符数据
        self.next: Optional['Block'] = None  # 下一个块的引用
    
    def size(self) -> int:
        """获取块的大小"""
        return len(self.data)
    
    def append(self, chars: List[str]) -> None:
        """向块末尾添加字符"""
        self.data.extend(chars)
    
    def insert(self, pos: int, chars: List[str]) -> None:
        """在指定位置插入字符"""
        self.data[pos:pos] = chars
    
    def delete(self, pos: int, length: int) -> None:
        """从指定位置删除指定长度的字符"""
        del self.data[pos:pos+length]
    
    def get(self, pos: int, length: int) -> List[str]:
        """获取从指定位置开始的指定长度的字符"""
        return self.data[pos:pos+length]

# 块状链表类
class BlockChain:
    def __init__(self):
        # 初始化分配池
        self.pool: List[int] = list(range(BNUM-1, 0, -1))
        self.head: Block = Block(0)  # 头块
        self.blocks: dict = {0: self.head}  # 块字典，用于快速访问
    
    def assign(self) -> int:
        """分配一个块编号"""
        if not self.pool:
            return -1
        return self.pool.pop()
    
    def recycle(self, block_id: int) -> None:
        """回收一个块编号"""
        self.pool.append(block_id)
        if block_id in self.blocks:
            del self.blocks[block_id]
    
    def find(self, pos: int) -> tuple:
        """
        查找文章中第pos个字符所在的块和块内位置
        时间复杂度：O(sqrt(n))
        :param pos: 字符位置（从1开始计数）
        :return: (块引用, 块内位置)
        """
        current: Optional[Block] = self.head
        while current and pos > current.size():
            pos -= current.size()
            current = current.next
        return current, pos
    
    def split(self, block: Optional[Block], pos: int) -> None:
        """
        分裂一个块，在指定位置处分裂
        时间复杂度：O(siz[block] - pos)
        :param block: 块引用
        :param pos: 分裂位置
        """
        # 如果块不存在或分裂位置在块末尾，则无需分裂
        if not block or pos == block.size():
            return
        
        # 分配新块
        new_block_id = self.assign()
        if new_block_id == -1:
            return
        
        new_block = Block(new_block_id)
        self.blocks[new_block_id] = new_block
        
        # 将block的pos位置之后的内容移动到新块
        new_block.data = block.data[pos:]
        block.data = block.data[:pos]
        
        # 连接新块
        new_block.next = block.next
        block.next = new_block
    
    def merge(self, block: Block, next_block: Block) -> None:
        """
        合并两个相邻的块
        时间复杂度：O(siz[next_block])
        :param block: 当前块
        :param next_block: 下一个块
        """
        # 将next_block的内容追加到block
        block.append(next_block.data)
        
        # 跳过next_block
        block.next = next_block.next
        
        # 回收next_block
        self.recycle(next_block.id)
    
    def maintain(self) -> None:
        """
        维护操作，合并相邻的小块
        时间复杂度：O(块数)
        """
        current: Optional[Block] = self.head
        while current and current.next:
            # 如果当前块和下一块的大小之和不超过块容量，则合并
            if current.size() + current.next.size() <= BLEN:
                next_block = current.next
                self.merge(current, next_block)
            else:
                current = current.next
    
    def insert(self, pos: int, chars: List[str]) -> None:
        """
        在指定位置插入字符串
        时间复杂度：O(sqrt(n) + len)
        :param pos: 插入位置
        :param chars: 插入的字符列表
        """
        length = len(chars)
        block, block_pos = self.find(pos)
        self.split(block, block_pos)
        
        current: Optional[Block] = block
        done = 0
        
        # 按块大小批量插入
        while done + BLEN <= length and current is not None:
            # 分配新块
            new_block_id = self.assign()
            if new_block_id == -1:
                break
            
            new_block = Block(new_block_id)
            self.blocks[new_block_id] = new_block
            
            # 写入一个完整块
            new_block.data = chars[done:done+BLEN]
            
            # 连接新块
            new_block.next = current.next
            current.next = new_block
            
            done += BLEN
            current = new_block
        
        # 插入剩余内容
        if length > done and current is not None:
            new_block_id = self.assign()
            if new_block_id != -1:
                new_block = Block(new_block_id)
                self.blocks[new_block_id] = new_block
                new_block.data = chars[done:]
                
                # 连接新块
                new_block.next = current.next
                current.next = new_block
        
        self.maintain()
    
    def erase(self, pos: int, length: int) -> None:
        """
        从指定位置删除指定长度的字符
        时间复杂度：O(sqrt(n) + len)
        :param pos: 删除起始位置
        :param length: 删除长度
        """
        block, block_pos = self.find(pos)
        self.split(block, block_pos)
        
        current: Optional[Block] = block
        if current is None:
            return
            
        next_block = current.next
        
        # 删除完整的块
        while next_block and length > next_block.size():
            length -= next_block.size()
            to_recycle = next_block
            next_block = next_block.next
            self.recycle(to_recycle.id)
        
        # 处理最后一个不完整的块
        if next_block:
            self.split(next_block, length)
            self.recycle(next_block.id)
            current.next = next_block.next
        else:
            current.next = None
        
        self.maintain()
    
    def get(self, pos: int, length: int) -> List[str]:
        """
        获取指定位置开始的指定长度的字符
        时间复杂度：O(sqrt(n) + len)
        :param pos: 获取起始位置
        :param length: 获取长度
        :return: 字符列表
        """
        result: List[str] = []
        block, block_pos = self.find(pos)
        done = 0
        
        # 获取第一个块的内容
        if block:
            first_chunk = block.get(block_pos, min(length, block.size() - block_pos))
            result.extend(first_chunk)
            done = len(first_chunk)
            block = block.next
        
        # 获取后续完整块的内容
        while block and done + block.size() <= length:
            result.extend(block.data)
            done += block.size()
            block = block.next
        
        # 获取最后一个不完整块的内容
        if block and done < length:
            remaining = length - done
            result.extend(block.get(0, remaining))
        
        return result
    
    def to_string(self) -> str:
        """将整个文本转换为字符串"""
        result: List[str] = []
        current: Optional[Block] = self.head
        while current:
            result.extend(current.data)
            current = current.next
        return ''.join(result)

def main():
    # 读取操作数
    n = int(input())
    pos = 0  # 光标位置
    editor = BlockChain()
    
    for _ in range(n):
        operation = input().split()
        op = operation[0]
        
        if op == "Prev":
            pos -= 1
        elif op == "Next":
            pos += 1
        elif op == "Move":
            pos = int(operation[1])
        elif op == "Insert":
            length = int(operation[1])
            # 读取插入的字符串，过滤非ASCII码字符
            chars: List[str] = []
            while len(chars) < length:
                ch = sys.stdin.read(1)
                if ch and 32 <= ord(ch) <= 126:
                    chars.append(ch)
            editor.insert(pos, chars)
        elif op == "Delete":
            length = int(operation[1])
            editor.erase(pos, length)
        elif op == "Get":
            length = int(operation[1])
            result = editor.get(pos, length)
            print(''.join(result))

if __name__ == "__main__":
    main()