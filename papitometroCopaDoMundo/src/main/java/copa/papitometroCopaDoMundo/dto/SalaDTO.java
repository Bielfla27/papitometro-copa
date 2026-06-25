package copa.papitometroCopaDoMundo.dto;

import copa.papitometroCopaDoMundo.entitites.Sala;

public class SalaDTO {

    private Long id;
    private String nome;
    private String codigo;
    private Long usuarioId;
    
    public SalaDTO() {
    }

    public SalaDTO(Sala entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.codigo = entity.getCodigo();

        if (entity.getDono() != null) {
            this.usuarioId = entity.getDono().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}