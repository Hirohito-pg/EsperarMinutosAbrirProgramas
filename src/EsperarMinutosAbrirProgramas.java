import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class EsperarMinutosAbrirProgramas extends JFrame implements KeyListener {
    private JFrame janela = new JFrame();
    private JLabel estado = new JLabel();
    private int tempo_atual;
    private boolean estado_tempo_espera = false;
    private boolean estado_aguarda_tempo = false;
    private int botoes_inicial = 3;
    private boolean iniciar_sem_confirmacao = false;
    private boolean musicas = false;

    public EsperarMinutosAbrirProgramas() {
        criarJanela();
        iniciarExecucao();
    }

    public static void main(String[] args) {
        EsperarMinutosAbrirProgramas esperar_minutos_abrir_programas = new EsperarMinutosAbrirProgramas();
    }

    public void criarJanela() {
        getJanela().setSize(200, 45);
        getJanela().setResizable(false);
        getJanela().setDefaultCloseOperation(3);
        getJanela().add(getEstado());
        getJanela().addKeyListener(this);
        getJanela().setVisible(true);
        alterarNomeBotoes("", "", "");
    }

    public void aguardaTempo(int tempo) {
        while (tempo > 0) if (isEstado_aguarda_tempo() != true) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                Logger.getLogger(EsperarMinutosAbrirProgramas.class.getName()).log(Level.SEVERE, null, ex);
            }
            tempo--;
        } else {
            setEstado_aguarda_tempo(false);
            tempo = 0;
        }
    }

    public void alterarNomeBotoes(String n1, String n2, String n3) {
        if ("".equals(n1)) UIManager.put("OptionPane.yesButtonText", "Sim");
        else {
            UIManager.put("OptionPane.yesButtonText", n1);
        }
        if ("".equals(n2)) UIManager.put("OptionPane.noButtonText", "Não");
        else {
            UIManager.put("OptionPane.yesButtonText", n2);
        }
        if ("".equals(n3)) UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        else UIManager.put("OptionPane.cancelButtonText", n3);
    }

    public void imprimiTempo(int tempo_espera_atual, int tempo_espera_padrao) {
        if ((getTempo_atual() >= 0) && (getTempo_atual() <= tempo_espera_padrao)) {
            if (tempo_espera_atual == 5) {
                getEstado().setText(MessageFormat.format("Falta {0}s", Integer.valueOf(tempo_espera_atual)));
                setTempo_atual(0);
            } else if (tempo_espera_atual == getTempo_atual()) {
                getEstado().setText(MessageFormat.format("Falta {0}s", Integer.valueOf(tempo_espera_atual)));
                setTempo_atual(getTempo_atual() - 15);
            } else if (tempo_espera_atual == tempo_espera_padrao - 5) {
                getEstado().setText("Comandos de Teclado - Ctrl+M");
            }
        }
    }

    public void localResidencia() {
        abrirGerenciador_Tarefas(2, 5);
        abrirASUS_Live_Update(2, 10);
        abrir3RVX(2, 5);
        abrirGlobo_Note(2, 5);
        abrirDropbox(2, 10);
        abrirOutlook(2, 0);
        if (isIniciar_sem_confirmacao() == true) {
            abrirTorrent(2, 10);
            abrirWindows_Media_Player(2, 10);
        } else if (!isIniciar_sem_confirmacao()) {
            abrirTorrent(1, 10);
            abrirWindows_Media_Player(1, 20);
        }
    }

    public void localFaculdade() {
        abrirGerenciador_Tarefas(2, 5);
        abrir3RVX(2, 0);
        abrirGlobo_Note(2, 5);
        if (isIniciar_sem_confirmacao() == true) {
            abrirADT_Bundle(2, 5);
            abrirUltrasurf(2, 10);
            abrirDropbox(2, 20);
            abrirOutlook(2, 5);
            abrirWindows_Media_Player(2, 10);
        } else if (!isIniciar_sem_confirmacao()) {
            abrirADT_Bundle(1, 5);
            abrirUltrasurf(1, 10);
            abrirDropbox(1, 20);
            abrirOutlook(1, 5);
            abrirWindows_Media_Player(1, 10);
        }
    }

    public void localTrabalho() {
        abrirGerenciador_Tarefas(2, 5);
        abrir3RVX(2, 2);
        if (isIniciar_sem_confirmacao() == true) {
            abrirGlobo_Note(2, 5);
            abrirDropbox(2, 20);
            abrirOutlook(2, 5);
            abrirWindows_Media_Player(2, 10);
        } else if (!isIniciar_sem_confirmacao()) {
            abrirGlobo_Note(1, 5);
            abrirDropbox(1, 20);
            abrirOutlook(1, 5);
            abrirWindows_Media_Player(1, 20);
        }
    }

    public void tempoAcabado(int tempo_espera, int opcao) {
        if (tempo_espera == 0) {
            getEstado().setText("Acabou Tempo");
            aguardaTempo(2);
            if (isMusicas()) {
                getEstado().setText("Músicas");
                abrirWindows_Media_Player(2, 5);
            } else if (opcao == 0) {
                getEstado().setText("Residência");
                localResidencia();
            } else if (opcao == 1) {
                getEstado().setText("Faculdade");
                localFaculdade();
            } else if (opcao == 2) {
                getEstado().setText("Trabalho");
                localTrabalho();
            }
            System.exit(0);
        }
    }

    public void ambienteUso(int tempo) {
        JOptionPane pane = new JOptionPane("Onde você esta?");
        final JDialog dialog = pane.createDialog(null, "Mensagem");
        dialog.setSize(315, 100);
        dialog.setModal(true);
        Timer timer = new Timer(tempo * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dialog.dispose();
            }
        });
        Object[] options = new Object[3];
        JButton residencia = new JButton("Residência");
        JButton faculdade = new JButton("Faculdade");
        JButton trabalho = new JButton("Trabalho");
        residencia.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                EsperarMinutosAbrirProgramas.this.AuxAmbienteUso(0);
            }
        });
        faculdade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                EsperarMinutosAbrirProgramas.this.AuxAmbienteUso(1);
            }
        });
        trabalho.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                EsperarMinutosAbrirProgramas.this.AuxAmbienteUso(2);
            }
        });
        options[0] = residencia;
        options[1] = faculdade;
        options[2] = trabalho;
        pane.setOptions(options);
        timer.setRepeats(false);
        timer.start();
        boolean estadoDialog = false;
        dialog.show();
        if (timer.isRunning()) {
            estadoDialog = true;
        }
        getEstado().setText("Iniciou a Contagem");
        if (!estadoDialog) {
            if (getBotoes_inicial() == 3) {
                setIniciar_sem_confirmacao(true);
                AuxAmbienteUso(0);
            }
        } else if (getBotoes_inicial() == 3) {
            AuxAmbienteUso(-1);
        }
        timer.stop();
    }

    public void AuxAmbienteUso(int v) {
        setBotoes_inicial(v);
        if (getBotoes_inicial() == -1) System.exit(0);
    }

    public void iniciarExecucao() {
        getEstado().setText("Abrindo Programas");
        int tempo_espera = 180;
        setTempo_atual(165);
        ambienteUso(60);
        for (; tempo_espera >= 0; tempo_espera -= 1) {
            if (isEstado_tempo_espera() == true) {
                tempo_espera = 0;
            }
            imprimiTempo(tempo_espera, 180);
            tempoAcabado(tempo_espera, getBotoes_inicial());
            aguardaTempo(1);
        }
    }

    public void abrirOutlook(int v, int tempo) {
        getEstado().setText("Outlook");
        aguardaTempo(tempo);
        String caminhoOutlook = "C:/Program Files/Microsoft Office/Office15/OUTLOOK.EXE";
        if (new File(caminhoOutlook).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja abrir o Outlook?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoOutlook));
                } catch (IOException e) {
                    getEstado().setText("Erro - Outlook");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Outlook!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoOutlook));
                } catch (IOException e) {
                    getEstado().setText("Erro - Outlook");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Outlook!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - Outlook");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o Outlook!", "Erro de Caminho", 0);
        }
    }

    public void abrirGlobo_Note(int v, int tempo) {
        getEstado().setText("GloboNote");
        aguardaTempo(tempo);
        String caminhoGlobo_Note = "C:/Program Files (x86)/GloboNote/GloboNote.exe";
        if (new File(caminhoGlobo_Note).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja abrir o GloboNote?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoGlobo_Note));
                } catch (IOException e) {
                    getEstado().setText("Erro - GloboNote");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o GloboNote!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoGlobo_Note));
                } catch (IOException e) {
                    getEstado().setText("Erro - GloboNote");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o GloboNote!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - GloboNote");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o GloboNote!", "Erro de Caminho", 0);
        }
    }

    public void abrir3RVX(int v, int tempo) {
        getEstado().setText("3RVX");
        aguardaTempo(tempo);
        String caminhoAux_Audio = "C:/Program Files (x86)/3RVX/3RVX.exe";
        if (new File(caminhoAux_Audio).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja abrir o 3RVX?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoAux_Audio));
                } catch (IOException e) {
                    getEstado().setText("Erro - 3RVX");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o 3RVX!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoAux_Audio));
                } catch (IOException e) {
                    getEstado().setText("Erro - 3RVX");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o 3RVX!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - 3RVX");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o 3RVX!", "Erro de Caminho", 0);
        }
    }

    public void abrirGerenciador_Tarefas(int v, int tempo) {
        getEstado().setText("Gerenciador de Tarefas");
        aguardaTempo(tempo);
        String caminhoGerenciador_Tarefas = "C:/Windows/System32/taskmgr.exe";
        if (new File(caminhoGerenciador_Tarefas).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja abrir o Gerenciador de Tarefas?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoGerenciador_Tarefas));
                } catch (IOException e) {
                    getEstado().setText("Erro - Gerenciador de Tarefas");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Gerenciador de Tarefas!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoGerenciador_Tarefas));
                } catch (IOException e) {
                    getEstado().setText("Erro - Gerenciador de Tarefas");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Gerenciador de Tarefas!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - Gerenciador de Tarefas");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o Gerenciador de Tarefas!", "Erro de Caminho", 0);
        }
    }

    public void abrirTorrent(int v, int tempo) {
        getEstado().setText("Torrent");
        aguardaTempo(tempo);
        String caminhoTorrent = "C:/Program Files (x86)/uTorrent/uTorrent.exe";
        if (new File(caminhoTorrent).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja fazer download no Torrent?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoTorrent));
                } catch (IOException e) {
                    getEstado().setText("Erro - Torrent");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Torrent!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoTorrent));
                } catch (IOException e) {
                    getEstado().setText("Erro - Torrent");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Torrent!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - Torrent");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o Torrent!", "Erro de Caminho", 0);
        }
    }

    public void abrirDropbox(int v, int tempo) {
        getEstado().setText("Dropbox");
        aguardaTempo(tempo);
        String caminhoDropbox = "C:/Users/hirohito/AppData/Roaming/Dropbox/bin/Dropbox.exe";
        if (new File(caminhoDropbox).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja abrir o Dropbox?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoDropbox));
                } catch (IOException e) {
                    getEstado().setText("Erro - Dropbox");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Dropbox!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoDropbox));
                } catch (IOException e) {
                    getEstado().setText("Erro - Dropbox");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Dropbox!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - Dropbox");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o Dropbox!", "Erro de Caminho", 0);
        }
    }

    public void abrirADT_Bundle(int v, int tempo) {
        getEstado().setText("ADT Bundle");
        aguardaTempo(tempo);
        String caminhoADTBundle = "D:/Meus Documentos/Faculdade/IDEs/ADT Bundle/Eclipse/eclipse.exe";
        if (new File(caminhoADTBundle).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja abrir o ADT Bundle?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoADTBundle));
                } catch (IOException e) {
                    getEstado().setText("Erro - ADT Bundle");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o ADT Bundle!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoADTBundle));
                } catch (IOException e) {
                    getEstado().setText("Erro - ADT Bundle");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o ADT Bundle!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - ADT Bundle");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o ADT Bundle!", "Erro de Caminho", 0);
        }
    }

    public void abrirASUS_Live_Update(int v, int tempo) {
        getEstado().setText("ASUS Live Update");
        aguardaTempo(tempo);
        String caminhoASUSLiveUpdate = "C:/Program Files (x86)/ASUS/ASUS Live Update/LiveUpdate.exe";
        if (new File(caminhoASUSLiveUpdate).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja abrir o ASUS Live Update?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoASUSLiveUpdate));
                } catch (IOException e) {
                    getEstado().setText("Erro - ASUS Live Update");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o ASUS Live Update!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoASUSLiveUpdate));
                } catch (IOException e) {
                    getEstado().setText("Erro - ASUS Live Update");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o ASUS Live Update!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - ASUS Live Update");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o ASUS Live Update!", "Erro de Caminho", 0);
        }
    }

    public void abrirUltrasurf(int v, int tempo) {
        getEstado().setText("Ultrasurf");
        aguardaTempo(tempo);
        String caminhoUltrasurf = "D:/Meus Documentos/Arquivos/Atalhos/Diretório de Programas/Ultrasurf/Ultrasurf 13.exe";
        if (new File(caminhoUltrasurf).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja abrir o Ultrasurf?");
                if (opcao == 0) try {
                    Desktop.getDesktop().open(new File(caminhoUltrasurf));
                } catch (IOException e) {
                    getEstado().setText("Erro - Ultrasurf");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Ultrasurf!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoUltrasurf));
                } catch (IOException e) {
                    getEstado().setText("Erro - Ultrasurf");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Ultrasurf!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - Ultrasurf");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o Ultrasurf!", "Erro de Caminho", 0);
        }
    }

    public String auxAbrir_Lista_Reproducao() {
        String caminho_padrao = "C:/Users/hirohito/Music/Listas de Reprodução";
        String comandoMusica = null;
        try {
            JFileChooser jFileChooser_seletor = new JFileChooser();
            jFileChooser_seletor.setFileSelectionMode(0);
            jFileChooser_seletor.setCurrentDirectory(new File(caminho_padrao));
            int retorno = jFileChooser_seletor.showOpenDialog(this);
            if (retorno == 0) comandoMusica = jFileChooser_seletor.getSelectedFile().getAbsolutePath();
        } catch (HeadlessException e) {
            getEstado().setText("Erro - Listas de Reprodução");
            JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir as Listas de Reprodução!", "Erro", 0);
        }
        return comandoMusica;
    }

    public void abrirWindows_Media_Player(int v, int tempo) {
        getEstado().setText("Windows Media Player");
        alterarNomeBotoes("", "", "Apenas Abrir");
        aguardaTempo(tempo);
        String caminhoWindows_Media_Player = "C:/Program Files (x86)/Windows Media Player/wmplayer.exe";
        if (new File(caminhoWindows_Media_Player).exists()) {
            if (v == 1) {
                int opcao = JOptionPane.showConfirmDialog(null, "Deseja ouvir música?");
                if (opcao == 0) try {
                    String lista_reproducao = auxAbrir_Lista_Reproducao();
                    if (lista_reproducao != null) Runtime.getRuntime().exec("cmd /c \"" + lista_reproducao + "\"");
                } catch (IOException e) {
                    getEstado().setText("Erro - Reprodução de Músicas");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Reproduzir Músicas!", "Erro", 0);
                }
                else if (opcao == 2) try {
                    Desktop.getDesktop().open(new File(caminhoWindows_Media_Player));
                } catch (IOException e) {
                    getEstado().setText("Erro - Windows Media Player");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Windows Media Player!", "Erro", 0);
                }
            } else if (v == 2) {
                try {
                    Desktop.getDesktop().open(new File(caminhoWindows_Media_Player));
                } catch (IOException e) {
                    getEstado().setText("Erro - Windows Media Player");
                    JOptionPane.showMessageDialog(null, "Não Foi Possível Abrir o Windows Media Player!", "Erro", 0);
                }
            }
        } else {
            getEstado().setText("Erro de Caminho - Windows Media Player");
            JOptionPane.showMessageDialog(null, "Impossível Abrir o Windows Media Player!", "Erro de Caminho", 0);
        }
    }

    public void reiniciar() {
        String caminho = "D:/Meus Documentos/Arquivos/Atalhos/Arquivos JAR/JAR/Esperar Minutos Para Abrir Programas/Esperar_Minutos_Para_Abrir_Programas.lnk";
        try {
            Desktop.getDesktop().open(new File(caminho));
        } catch (IOException e) {
            getEstado().setText("Erro");
            JOptionPane.showMessageDialog(null, "Não Foi Possível Reiniciar!", "Erro", 0);
            System.exit(0);
        }
    }

    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == 80) && (e.isControlDown())) {
            System.exit(0);
        }
        if ((e.getKeyCode() == 84) && (e.isControlDown())) {
            JOptionPane.showMessageDialog(null, "Tempo de espera cancelado!");
            setEstado_tempo_espera(true);
        }
        if ((e.getKeyCode() == 69) && (e.isControlDown())) {
            setEstado_aguarda_tempo(true);
        }
        if ((e.getKeyCode() == 83) && (e.isControlDown())) {
            JOptionPane.showMessageDialog(null, "Programas serão inicializados\nsem confirmação!");
            setIniciar_sem_confirmacao(true);
        }
        if ((e.getKeyCode() == 67) && (e.isControlDown())) {
            JOptionPane.showMessageDialog(null, "Programas serão inicializados\ncom confirmação!");
            setIniciar_sem_confirmacao(false);
        }
        if ((e.getKeyCode() == 65) && (e.isControlDown())) {
            if (!isMusicas()) {
                JOptionPane.showMessageDialog(null, "Com apenas músicas!");
                setMusicas(true);
            } else {
                JOptionPane.showMessageDialog(null, "Sem apenas músicas!");
                setMusicas(false);
            }
        }
        if ((e.getKeyCode() == 82) && (e.isControlDown())) {
            JOptionPane.showMessageDialog(null, "Programas será reiniciado!");
            reiniciar();
            System.exit(0);
        }
        if ((e.getKeyCode() == 77) && (e.isControlDown()))
            JOptionPane.showMessageDialog(null, "Sair/Parar! - Ctrl+P\nCancelar Tempo Geral! - Ctrl+T\nCancelar Aguardo de Tempo! - Ctrl+E\nSem Confirmação! - Ctrl+S\nCom Confirmação! - Ctrl+C\nApenas Músicas! - Ctrl+A\nReiniciar! - Ctrl+R\nMostrar Eventos de Teclado! - Ctrl+M");
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    private JFrame getJanela() {
        return this.janela;
    }

    private void setJanela(JFrame janela) {
        this.janela = janela;
    }

    private JLabel getEstado() {
        return this.estado;
    }

    private void setEstado(JLabel estado) {
        this.estado = estado;
    }

    private int getTempo_atual() {
        return this.tempo_atual;
    }

    private void setTempo_atual(int tempo_atual) {
        this.tempo_atual = tempo_atual;
    }

    private boolean isEstado_tempo_espera() {
        return this.estado_tempo_espera;
    }

    private void setEstado_tempo_espera(boolean estado_tempo_espera) {
        this.estado_tempo_espera = estado_tempo_espera;
    }

    private boolean isEstado_aguarda_tempo() {
        return this.estado_aguarda_tempo;
    }

    private void setEstado_aguarda_tempo(boolean estado_aguarda_tempo) {
        this.estado_aguarda_tempo = estado_aguarda_tempo;
    }

    private int getBotoes_inicial() {
        return this.botoes_inicial;
    }

    private void setBotoes_inicial(int botoes_inicial) {
        this.botoes_inicial = botoes_inicial;
    }

    private boolean isIniciar_sem_confirmacao() {
        return this.iniciar_sem_confirmacao;
    }

    private void setIniciar_sem_confirmacao(boolean iniciar_sem_confirmacao) {
        this.iniciar_sem_confirmacao = iniciar_sem_confirmacao;
    }

    private boolean isMusicas() {
        return this.musicas;
    }

    private void setMusicas(boolean musicas) {
        this.musicas = musicas;
    }
}