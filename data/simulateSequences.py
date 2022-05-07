import random

aas = "ABCDEFGHIKLMNPQRSTUVWXYZ"

def simulate(patterns, reference, numall, numtarget):
    res = []
    f = open(reference, "r")
    ref = "".join([x.strip() for x in f.read().split("\n")[1:]])
    f.close()
    f = open(patterns, "r")
    pat = [(int(x.split(";")[0][:-1]),x.split(";")[0][-1]) for x in f.read().split("\n")[2:] if len(x.strip()) != 0]
    f.close()
    for x in range(0, numtarget):
        selected = pat[random.randint(0, len(pat)-1)]
        mut = [x for x in ref]
        mut[selected[0]] = selected[1]
        res += ["".join(mut)]
    for x in range(0, numall-numtarget):
        nummut = random.randint(1,4)
        mut = [x for x in ref]
        for i in range(0, nummut):
            pos = random.randint(0, len(ref)-1)
            aa = aas[random.randint(0, len(aas)-1)]
            mut[pos] = aa
        res += ["".join(mut)]
    return res

random.seed(28167)
seqs = simulate("GREB1_patterns.csv", "GREB1_reference.fasta", 400, 20)
fasta = open("site_sequences.fasta", "w")
fastq = open("site_sequences.fastq", "w")
for i in range(0, len(seqs)):
    fasta.write(">sequence_" + str(i) + "\n")
    fasta.write(seqs[i] + "\n")
    fastq.write("@sequence_" + str(i) + "\n")
    fastq.write(seqs[i] + "\n")
    fastq.write("+\n")
    fastq.write("".join([chr(random.randint(33, 73)) for x in range(0, len(seqs[i])-1)]))
    fastq.write("+\n")
fasta.close()
fastq.close()
