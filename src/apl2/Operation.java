package apl2;

/**
 * Classe Operation - métodos estáticos que realizam operações na base de dados.
 *
 * ATENÇÃO: As assinaturas dos métodos desta classe NÃO devem ser alteradas!
 *
 * Regras de negócio:
 *   - Ausência de nota: nota == 99.9f
 *   - Nota válida: nota no intervalo [0.0, 10.0]
 *   - Prefixo do ID novo: "26.S1-" (ano 26, semestre 1)
 *   - Separador novo: ponto-e-vírgula (;)
 *   - Separador legado: cerquilha (#)
 */
public class Operation {

    // Constante que representa ausência de nota no novo sistema
    private static final float NOTA_AUSENTE = 99.9f;

    // Prefixo do ID no novo sistema (ano 2026, 1º semestre)
    private static final String ID_PREFIXO = "26.S1-";

    /**
     * map(LinkedListOriginal)
     *
     * Percorre a lista do sistema legado (LinkedListOriginal com NodeOriginal)
     * e mapeia cada entrada para o novo formato (DLinkedList com Node).
     *
     * Conversão do ID:  "999"        ->  "26.S1-999"
     * Conversão da nota: inteiro=8, decimal=7  ->  8.7f
     *                    inteiro=-1 OU decimal=-1  ->  99.9f (ausência)
     *
     * A ordem dos nós é preservada usando append() (inserção no final).
     *
     * @param listaOriginal lista do sistema legado
     * @return nova DLinkedList com os dados convertidos
     */
    public static DLinkedList map(LinkedListOriginal listaOriginal) {
        DLinkedList novaLista = new DLinkedList();

        // Percorre a lista original pelo ponteiro prox (simplesmente encadeada)
        NodeOriginal atual = listaOriginal.getHead();

        while (atual != null) {
            // Monta novo ID com prefixo
            String novoId = ID_PREFIXO + atual.getId();

            // Converte as partes da nota para float
            float nota;
            int parteInteira  = atual.getNotaInt();
            int parteDecimal  = atual.getNotaDec();

            if (parteInteira == -1 || parteDecimal == -1) {
                // Ausência de nota
                nota = NOTA_AUSENTE;
            } else {
                // Combina as partes: inteiro.decimal
                nota = parteInteira + parteDecimal / 10.0f;
            }

            // Insere no final para preservar a ordem original
            novaLista.append(novoId, atual.getNome(), nota);

            atual = atual.getProx();
        }

        return novaLista;
    }

    /**
     * filterRemoveNonGraded(DLinkedList)
     *
     * Recebe a lista mapeada e retorna uma NOVA lista contendo apenas
     * os nós com notas VÁLIDAS (remove os nós com nota == 99.9).
     *
     * @param lista lista com todos os dados mapeados
     * @return nova DLinkedList apenas com notas válidas
     */
    public static DLinkedList filterRemoveNonGraded(DLinkedList lista) {
        DLinkedList listaFiltrada = new DLinkedList();

        Node atual = lista.getHead();

        while (atual != null) {
            // Inclui apenas quem TEM nota válida (diferente de 99.9)
            if (atual.getNota() != NOTA_AUSENTE) {
                listaFiltrada.append(atual.getId(), atual.getNome(), atual.getNota());
            }
            atual = atual.getProx();
        }

        return listaFiltrada;
    }

    /**
     * filterRemoveGraded(DLinkedList)
     *
     * Recebe a lista mapeada e retorna uma NOVA lista contendo apenas
     * os nós com AUSÊNCIA de nota (nota == 99.9).
     *
     * @param lista lista com todos os dados mapeados
     * @return nova DLinkedList apenas com ausências de nota
     */
    public static DLinkedList filterRemoveGraded(DLinkedList lista) {
        DLinkedList listaFiltrada = new DLinkedList();

        Node atual = lista.getHead();

        while (atual != null) {
            // Inclui apenas quem NÃO tem nota (99.9)
            if (atual.getNota() == NOTA_AUSENTE) {
                listaFiltrada.append(atual.getId(), atual.getNome(), atual.getNota());
            }
            atual = atual.getProx();
        }

        return listaFiltrada;
    }

    /**
     * filterRemoveBelowAverage(DLinkedList, float)
     *
     * Recebe a lista de notas válidas (resultado de filterRemoveNonGraded)
     * e a média calculada por reduce(), e retorna uma NOVA lista contendo
     * apenas os nós com notas ACIMA da média (nota > media).
     *
     * Nós com nota igual à média são removidos (abaixo ou igual = fora).
     *
     * @param lista lista com notas válidas
     * @param media média das notas válidas calculada por reduce()
     * @return nova DLinkedList apenas com notas acima da média
     */
    public static DLinkedList filterRemoveBelowAverage(DLinkedList lista, float media) {
        DLinkedList listaFiltrada = new DLinkedList();

        Node atual = lista.getHead();

        while (atual != null) {
            // Inclui apenas quem está ACIMA da média
            if (atual.getNota() > media) {
                listaFiltrada.append(atual.getId(), atual.getNome(), atual.getNota());
            }
            atual = atual.getProx();
        }

        return listaFiltrada;
    }

    /**
     * reduce(DLinkedList)
     *
     * Recebe a lista de notas válidas (resultado de filterRemoveNonGraded)
     * e calcula a média aritmética das notas.
     *
     * Se a lista estiver vazia, retorna 0.0f para evitar divisão por zero.
     *
     * @param lista lista com notas válidas
     * @return média das notas como float
     */
    public static float reduce(DLinkedList lista) {
        if (lista.isEmpty()) {
            return 0.0f;
        }

        float soma  = 0.0f;
        int   count = 0;

        Node atual = lista.getHead();

        while (atual != null) {
            soma  += atual.getNota();
            count++;
            atual  = atual.getProx();
        }

        return soma / count;
    }

    /**
     * mapToString(DLinkedList)
     *
     * Percorre todos os nós da lista e os serializa para uma única String
     * no formato CSV, pronta para ser salva em arquivo:
     *
     *   ID;Nome;Nota\n
     *   ID;Nome;Nota\n
     *   ...
     *
     * Cada dado de uma pessoa é separado por ponto-e-vírgula (;).
     * Cada pessoa é separada por quebra de linha (\n).
     *
     * O método usa o toString() do Node, que já retorna "id;nome;nota".
     *
     * @param lista lista com os dados a serializar
     * @return String com o conteúdo completo da lista em formato CSV
     */
    public static String mapToString(DLinkedList lista) {
        StringBuilder sb = new StringBuilder();

        Node atual = lista.getHead();

        while (atual != null) {
            sb.append(atual.toString()); // "id;nome;nota"
            sb.append("\n");
            atual = atual.getProx();
        }

        return sb.toString();
    }
}
