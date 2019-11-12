use ExtUtils::MakeMaker;

WriteMakefile(NAME => 'sowhat-dk',
              clean => {FILES => 'test.output'},
              EXE_FILES => ['sowhat-dk']);

