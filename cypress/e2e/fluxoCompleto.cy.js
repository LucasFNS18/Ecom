describe('Fluxo Completo - Ecom Supplier', () => {
  const baseUrl = 'http://127.0.0.1:5500/src/main/resources/static/Frontend';
  const email = `usuario${Date.now()}@teste.com`;
  const senha = '123456';
  let token = '';

  const anuncios = [
    {
      title: 'Anúncio Teste 1',
      description: 'Descrição Teste 1',
      category: 'Categoria 1',
      imageUrl: 'https://via.placeholder.com/150'
    },
    {
      title: 'Anúncio Teste 2',
      description: 'Descrição Teste 2',
      category: 'Categoria 2',
      imageUrl: 'https://via.placeholder.com/150'
    }
  ];

  it('Deve realizar o registro com sucesso', () => {
    cy.visit(`${baseUrl}/register.html`);
    cy.get('#email').type(email);
    cy.get('#password').type(senha);
    cy.on('window:alert', (msg) => {
      expect(msg).to.contains('Cadastro realizado com sucesso!');
    });
    cy.get('form#registerForm').submit();
    cy.url().should('include', 'login.html');
  });

  it('Deve falhar ao logar com credenciais erradas', () => {
    cy.visit(`${baseUrl}/login.html`);

    const tentativas = [
      { email: 'errado@teste.com', senha },
      { email, senha: 'errada123' },
      { email: 'errado@teste.com', senha: 'errada123' }
    ];

    tentativas.forEach(({ email: e, senha: s }) => {
      cy.get('#email').clear().type(e);
      cy.get('#password').clear().type(s);
      cy.on('window:alert', (msg) => {
        expect(msg).to.contains('Email ou senha inválidos');
      });
      cy.get('form#loginForm').submit();
    });
  });

  it('Deve realizar login com sucesso e obter token', () => {
    cy.visit(`${baseUrl}/login.html`);
    cy.get('#email').type(email);
    cy.get('#password').type(senha);
    cy.on('window:alert', (msg) => {
      expect(msg).to.contains('Login realizado com sucesso');
    });
    cy.window().then((win) => {
      cy.stub(win, 'alert').callsFake((msg) => {
        expect(msg).to.contains('Login realizado com sucesso');
      });
    });
    cy.get('form#loginForm').submit();

    cy.url().should('include', 'gerenciaranuncios.html');

    // Captura o token armazenado
    cy.window().then((win) => {
      token = win.localStorage.getItem('token');
      expect(token).to.not.be.null;
    });
  });

  it('Deve criar, editar, visualizar e excluir anúncios com sucesso', () => {
    // Set token
    cy.window().then((win) => {
      win.localStorage.setItem('token', token);
    });

    cy.visit(`${baseUrl}/gerenciaranuncios.html`);

    // Criação e edição dos 2 anúncios
   anuncios.forEach((anuncio) => {
  cy.wait(1000);

  cy.get('#title').clear().type(anuncio.title);
  cy.get('#description').clear().type(anuncio.description);
  cy.get('#category').clear().type(anuncio.category);
  cy.get('#imageUrl').clear().type(anuncio.imageUrl);

  cy.get('#adForm').submit();

  cy.wait(3000);

  // Confirmar que o anúncio aparece na lista
  cy.get('#adsList').should('contain', anuncio.title);

  // Editar o anúncio recém criado
  cy.get('#adsList').contains(anuncio.title).parent().parent().within(() => {
    cy.contains('Editar').click();
  });

  cy.get('#editTitle').clear().type(`${anuncio.title} Editado`);
  cy.get('#editDescription').clear().type(`${anuncio.description} Editada`);
  cy.get('#editCategory').clear().type(`${anuncio.category} Editada`);
  cy.get('#editImageUrl').clear().type('https://postimage.me/images/2025/05/21/Imagem-do-WhatsApp-de-2025-05-16-as-10.29.14_df53a677.jpg');

  cy.get('#editForm').submit();

  cy.wait(2000);
});

    // Clicar no botão "Ver Anúncio" (simula navegação)
    cy.visit(`${baseUrl}/fornecedores.html`);
    cy.wait(3000);
    cy.get('#suppliersGrid').should('exist');

    // Voltar para gerenciamento
    cy.visit(`${baseUrl}/gerenciaranuncios.html`);
    cy.wait(3000);

    // Excluir o primeiro anúncio
    cy.get('#adsList button').contains('Excluir').first().click();
    cy.wait(2000);

    // Confirmar que ele foi removido
    cy.get('#adsList').should('not.contain', `${anuncios[0].title} Editado`);

    // Ver novamente na página de fornecedores
    cy.visit(`${baseUrl}/fornecedores.html`);
    cy.wait(3000);
    cy.get('#suppliersGrid').should('exist');
  });
});
