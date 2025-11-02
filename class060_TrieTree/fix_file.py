# 修复Code01_TrieTree.java文件的脚本
with open('Code01_TrieTree.java', 'r', encoding='utf-8') as f:
    lines = f.readlines()

# 删除最后一行（多余的右花括号）
lines = lines[:-1]

# 写回文件
with open('Code01_TrieTree.java', 'w', encoding='utf-8') as f:
    f.writelines(lines)

print("文件修复完成")