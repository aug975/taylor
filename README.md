# Atividade Projeto de Computação Paralela - Turma 05G Lab 2022

![image](https://user-images.githubusercontent.com/101229028/196855039-39c0fc25-d76c-4afa-a675-20524005b796.png)

## Grupo:
Augusto Rassi Scrideli 42023092

## Planejamento
Para execução do exercício proposto, foram testadas várias alternativas buscando atender as variáveis definidas.
Um grande desafio é que a série de Taylor do logaritmo natural com o uso de fatorial crescente no denominador, leva a valores extremamente pequenos para os novos termos da série já após as primeiras iterações. Com as limitações de casa decimais dos tipos numéricos padrão do C e C++, tipo Double ou Long Double, conseguia-se chegar a um número máximo de poucas dezenas de termos, muito aquém do desafio de uma série com 1.000.000 de termos.
A forma pensada para contornar esta limitação da precisão, sem ter que criar algum tipo de algoritmo complexo com uso de arrays longos e temporários, foi buscar uma classe que realizasse transformações de ponto flutuante de precisão arbitrária, guardando valores mais exatos em base decimal.

Inicialmente tentou-se o tipo Decimal na linguagem Python, que atendeu plenamente o requisito de precisão dos dados, mas mostrou-se extremamente lento a medida que se aumentava o número de termos e de casa decimais com a progressão fatorial. Com a paralelizarão de processos ( dada a limitação do paralelismo de threads no Python) houve uma melhora relevante na performance, mas muito distante do necessário para escalar o número de termos.

Finalmente buscou-se uma opção com o tipo BigDecimal na linguagem Java, que também resolveu o tema da precisão, mas com eficiência muito maior pela própria característica da linguagem. O paralelismo foi implementado pelo instanciamento  de classes, uma vez que não há opção de Posix threads para o Java, com ganhos interessantes de performance, como demonstrado pelas execuções do programa.

## Informações sobre execução do programa
### Compilação
O programa foi compilado para testes na plataforma Eclipse (IDE de Java em Desktop Offline).
Máquina em que foi testado apresenta 6 cores (12 threads) com 64Gb de memória.
Alguns testes preliminares foram realizados em Replit, mas observa-se que devido à falta de recursos disponíveis no ambiente do replit, o paralelismo não era implementado de forma eficiente.
### Execução
O programa foi testado em Eclipse. Não há parâmetros específicos necessários, o programa deve rodar no IDE padrão.

Obs.: Pode ser necessária a instalação das bibliotecas especificadas no header.
Os logs gerados nas execuções de teste estão em imagens mais abaixo. Foram tirados diretamente do output do console no IDE. Para replicar estes testes, pode-se simplesmente modificar os valores de T e precisão para que sejam equivalentes aos especificados em cada teste.

![image](https://user-images.githubusercontent.com/101229028/196857771-c9c5c340-fe85-4cdd-b35e-94bcfcff4c3d.png)

## Sobre o programa (V2)
O código foi construído em linguagem Java com utilização das classes BigDecimal e BigInteger para alcançar uma precisão desejada. As operações matemáticas utilizadas são também específicas dessas classes.

Foi adotada uma solução de paralelismo através da qual se criou oito instâncias de uma classe Tarefa, sendo cada uma delas responsável por processar simultâneamente a mesma proporção de termos da série de Taylor. Ao final da execução da última instância, os oitos valores obtidos são somados para se chegar ao resultado final. 

No mesmo código, na sequência da execução paralela, é feita uma nova execução com uma única instância para permitir a comparação dos tempos de execução (com e sem paralelismo). 

Obs.: Uma otimização possível que não foi implementada nessa versão seria a construção de um modelo onde a precisão a ser utilizada fosse variável de acordo com o número de casas decimais dos termos sendo adicionados naquele instante. Com isso, poderia se obter um ganho adicional de performance expressivo. 

### Execução dos Testes
Foram realizadas observações com diferentes valores de números de termos e precisões (número de casas decimais) para entendimento do comportamento do tempo de execução em relação às variáveis. Em situações de menor precisão, muitas vezes os termos adicionais da série atingiam valor zero em relação à mesma, deixando de contribuir com a composição do valor final. 

### Cenários de Teste
Obs.: Estes testes demoram para concluir. Testes com prints de tela ainda não inclusos ainda estão finalizando e serão inclusos posteriormente.

100.000 termos com precisão 10.000

100.000 termos com precisão 50.000

1.000.000 termos com precisão de 100

1.000.000 termos com precisão de 1.000

1.000.000 termos com precisão 10.000

1.000.000 termos com precisão 100.000
