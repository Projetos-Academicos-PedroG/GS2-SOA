package br.com.fiap.pedro.domain.model;

public enum AcaoHoraDeOuro {
    BLOQUEAR_LINHA(1, "Bloquear a linha (chip) na operadora",
            "Ligue para a operadora a partir de outro telefone e peça o bloqueio do chip para impedir uso de SMS de verificação.", 10),
    BLOQUEAR_BANCOS(2, "Bloquear apps bancários e cartões",
            "Contate os bancos (telefone ou internet banking em outro aparelho) e bloqueie acessos, Pix e cartões cadastrados no celular.", 15),
    APAGAR_REMOTAMENTE(3, "Localizar e apagar o aparelho remotamente",
            "Use o Encontre meu dispositivo (Android) ou Buscar iPhone (iOS) para bloquear a tela e apagar os dados.", 20),
    TROCAR_SENHAS(4, "Trocar senhas de e-mail e redes sociais",
            "Altere as senhas do e-mail principal, WhatsApp, redes sociais e encerre sessões ativas no aparelho perdido.", 30),
    REGISTRAR_BO(5, "Registrar Boletim de Ocorrência online",
            "Registre o B.O. na Delegacia Eletrônica do seu estado informando o IMEI do aparelho.", 45),
    BLOQUEAR_IMEI(6, "Solicitar bloqueio do IMEI",
            "Com o número do B.O., peça à operadora o bloqueio do IMEI no cadastro nacional (Anatel/CEMI).", 60),
    AVISAR_CONTATOS(7, "Avisar contatos sobre possível golpe",
            "Avise familiares e amigos para ignorarem pedidos de dinheiro vindos do seu número ou perfis.", 60);

    private final int ordem;
    private final String titulo;
    private final String descricao;
    private final int prazoMinutos;

    AcaoHoraDeOuro(int ordem, String titulo, String descricao, int prazoMinutos) {
        this.ordem = ordem;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prazoMinutos = prazoMinutos;
    }

    public int getOrdem() { return ordem; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public int getPrazoMinutos() { return prazoMinutos; }
}
