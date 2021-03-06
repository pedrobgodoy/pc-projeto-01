package vila;

import propriedades.construcao.PropriedadesTemplo;
import tela.Tela;

import java.awt.*;
import java.util.ArrayList;

public class Templo {
    private final Vila vila;
    private int fe;
    private EstadoTemplo estado;
    private final ArrayList<String> ataques;
    private PropriedadesTemplo props;

    private boolean protecaoNuvemGafanhotos;
    private boolean protecaoMortePrimogenitos;
    private boolean protecaoChuvaDePedras;

    public Templo(Vila vila) {
        this.vila = vila;
        this.estado = EstadoTemplo.PARADO;
        this.ataques = new ArrayList<>();
        this.props = vila.props.templo;

        Tela.i.habilitarTemplo();
        Tela.i.mostrarTemplo("parado", Color.WHITE);
    }

    public void orar() {
        try {
            Thread.sleep(this.vila.props.templo.getTempoUso());
            this.adicionarFe(this.vila.props.templo.getProducaoPorCiclo());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void registrarSacrificio() {
        this.adicionarFe(this.vila.props.templo.getProducaoPorSacrifio());
    }

    private void adicionarFe(int quantidade) {
        this.fe += quantidade;
        Tela.i.mostrarOferendaFe(this.fe);
    }

    public void comandoTemploEvoluir(String evolucao) {
        if(this.estado != EstadoTemplo.PARADO) return;
        switch (evolucao) {
            case "Nuvem de gafanhotos" -> new Thread(this::evoluirNuvemDeGafanhotos).start();
            case "Morte dos primogênitos" -> new Thread(this::evoluirMorteDosPrimogenitos).start();
            case "Chuva de pedras" -> new Thread(this::evoluirChuvaDePedras).start();
            case "Proteção contra nuvem de gafanhotos" -> new Thread(this::evoluirProtecaoNuvemDeGafanhotos).start();
            case "Proteção contra morte dos primogênitos" -> new Thread(this::evoluirProtecaoMorteDosPrimogenitos).start();
            case "Proteção contra chuva de pedras" -> new Thread(this::evoluirProtecaoChuvaDePedras).start();
        }
    }

    private void evoluirNuvemDeGafanhotos() {
        if(this.ataques.contains("NUVEM_GAFANHOTOS")) {
            Tela.i.mostrarMensagemErro("Alerta templo", "Você já fez esta evolução!");
            return;
        }
        int custoFe = this.props.evNuvemGafanhoto.getCustoFe();
        int custoTempo = this.props.evNuvemGafanhoto.getTempoEvolucao();
        boolean evoluiu = this.evoluir(custoFe, custoTempo, "Nuvem de gafanhotos");
        if(!evoluiu) {
            return;
        }
        this.ataques.add("NUVEM_GAFANHOTOS");
        Tela.i.mostrarAtaques(this.ataques);
    }

    private void evoluirMorteDosPrimogenitos() {
       if(this.ataques.contains("MORTE_PRIMOGENITOS")) {
            Tela.i.mostrarMensagemErro("Alerta templo", "Você já fez esta evolução!");
            return;
        }
        int custoFe = this.props.evMortePrimogenitos.getCustoFe();
        int custoTempo = this.props.evMortePrimogenitos.getTempoEvolucao();
        boolean evoluiu = this.evoluir(custoFe, custoTempo, "Morte dos primogênitos");
        if(!evoluiu) {
            return;
        }
        this.ataques.add("MORTE_PRIMOGENITOS");
        Tela.i.mostrarAtaques(this.ataques);
    }

    private void evoluirChuvaDePedras() {
        if(this.ataques.contains("CHUVA_PEDRAS")) {
            Tela.i.mostrarMensagemErro("Alerta templo", "Você já fez esta evolução!");
            return;
        }
        int custoFe = this.props.evChuvaPedras.getCustoFe();
        int custoTempo = this.props.evChuvaPedras.getTempoEvolucao();
        boolean evoluiu = this.evoluir(custoFe, custoTempo, "Chuva de pedras");
        if(!evoluiu) {
            return;
        }
        this.ataques.add("CHUVA_PEDRAS");
        Tela.i.mostrarAtaques(this.ataques);
    }

    private void evoluirProtecaoNuvemDeGafanhotos() {
        int custoFe = this.props.evProtecaoNuvemGafanhoto.getCustoFe();
        int custoTempo = this.props.evNuvemGafanhoto.getTempoEvolucao();
        this.evoluir(custoFe, custoTempo, "Proteção contra nuvem de gafanhotos");
        this.protecaoNuvemGafanhotos = true;
    }

    private void evoluirProtecaoMorteDosPrimogenitos() {
        int custoFe = this.props.evProtecaoMortePrimogenitos.getCustoFe();
        int custoTempo = this.props.evProtecaoMortePrimogenitos.getTempoEvolucao();
        this.evoluir(custoFe, custoTempo, "Proteção contra morte dos primogênitos");
        this.protecaoMortePrimogenitos = true;
    }

    private void evoluirProtecaoChuvaDePedras() {
        int custoFe = this.props.evProtecaoChuvaPedras.getCustoFe();
        int custoTempo = this.props.evProtecaoChuvaPedras.getTempoEvolucao();
        this.evoluir(custoFe, custoTempo, "Proteção contra chuva de pedras");
        this.protecaoChuvaDePedras = true;
    }

    private boolean evoluir(int custoFe, int tempo, String evolucao) {
        if(!this.consumirFe(custoFe)) {
            Tela.i.mostrarMensagemErro("Alerta Templo", "Você não possui fé o suficiente!");
            return false;
        }
        Tela.i.mostrarTemplo("Evoluindo " + evolucao, Color.BLUE);
        this.estado = EstadoTemplo.EVOLUINDO;
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Tela.i.mostrarTemplo("parado", Color.WHITE);
        this.estado = EstadoTemplo.PARADO;
        return true;
    }

    public boolean consumirFe(int quantidade) {
        if(this.fe < quantidade) {
            return false;
        }
        this.fe -= quantidade;
        Tela.i.mostrarOferendaFe(this.fe);
        return true;
    }

    public boolean temProtecaoNuvemGafanhotos() {
        return protecaoNuvemGafanhotos;
    }

    public void setProtecaoNuvemGafanhotos(boolean protecaoNuvemGafanhotos) {
        this.protecaoNuvemGafanhotos = protecaoNuvemGafanhotos;
    }

    public boolean temProtecaoMortePrimogenitos() {
        return protecaoMortePrimogenitos;
    }

    public void setProtecaoMortePrimogenitos(boolean protecaoMortePrimogenitos) {
        this.protecaoMortePrimogenitos = protecaoMortePrimogenitos;
    }

    public boolean temProtecaoChuvaDePedras() {
        return protecaoChuvaDePedras;
    }

    public void setProtecaoChuvaDePedras(boolean protecaoChuvaDePedras) {
        this.protecaoChuvaDePedras = protecaoChuvaDePedras;
    }
}
